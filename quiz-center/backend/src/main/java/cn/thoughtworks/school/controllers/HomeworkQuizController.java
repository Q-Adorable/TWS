package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.*;
import cn.thoughtworks.school.enums.JobState;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.HomeworkQuizRepository;
import cn.thoughtworks.school.repositories.HomeworkSubmissionRepository;
import cn.thoughtworks.school.repositories.QuizRepository;
import cn.thoughtworks.school.repositories.UserQuizGroupRepository;
import cn.thoughtworks.school.requestParams.CreateHomeworkParam;
import cn.thoughtworks.school.services.AmazonClientService;
import cn.thoughtworks.school.services.UserCenterService;
import cn.thoughtworks.school.services.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/v3/homeworkQuizzes")
@Slf4j
public class HomeworkQuizController {

    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private HomeworkQuizRepository homeworkQuizRepository;
    @Autowired
    private UserQuizGroupRepository userQuizGroupRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Value("${homework-quiz-jenkins-url.add}")
    private String addHomeworkQuizJenkinsUrl;

    @Value("${homework-quiz-jenkins-url.add-log}")
    private String addHomeworkLog;

    @Value("${homework-quiz-jenkins-url.submission-log}")
    private String submissionHomeworkLog;

    @Value("${quiz-center-url}")
    private String quizCenterUrl;
    @Value("${homework-type}")
    private String type;

    @Value("${jenkins-callback-status.success}")
    private int saveSuccessStatus;
    @Value("${jenkins-callback-status.line-up}")
    private int lineUp;
    @Value("${jenkins-callback-status.failure}")
    private int saveFailure;
    @Value("${jenkins-callback-status.loading}")
    private int loading;
    @Value("${jenkins-callback-status.progress}")
    private int progress;

    @Autowired
    private Utils utils;

    @Autowired
    UserCenterService userCenterService;

    @Autowired
    private HomeworkSubmissionRepository homeworkSubmissionRepository;

    private String githubToken = "ca56d5b5467bc58ed784d14483bc233e87291fd0";

    @Autowired
    HomeworkQuizController(AmazonClientService amazonClientService, ResourceLoader resourceLoader) {
        this.amazonClientService = amazonClientService;
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.GET)
    public ResponseEntity getCoding(@PathVariable String ids, @RequestParam(value = "status", defaultValue = "") String status) throws BusinessException {

        List<Long> idList = Arrays.stream(ids.split(","))
                .map(id -> Long.parseLong(id))
                .collect(Collectors.toList());

        List<HomeworkQuiz> homeworkQuizzes = homeworkQuizRepository.findByIdInAndIsAvailableIsTrue(idList);

        if (status.equals("")) {
            if (homeworkQuizzes.size() == 0) {
                throw new BusinessException("该编程题不存在");
            }
            List<Map> users = userCenterService.getUsersByIds(homeworkQuizzes.get(0).getMakerId().toString());
            return new ResponseEntity<>(utils.formatEntry(homeworkQuizzes.get(0), users), HttpStatus.OK);
        }
        return new ResponseEntity<>(utils.formatList(homeworkQuizzes), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getHomeworkQuizzes(@RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                             @RequestParam(value = "tags", defaultValue = "") String tags,
                                             @RequestParam(value = "makerId", defaultValue = "0") Long makerId,
                                             @RequestParam(value = "status", defaultValue = "-1") int status, @Auth User current) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<HomeworkQuiz> homeworkQuizzes = null;

        List roles = current.getRoles();
        List<UserQuizGroup> userQuizGroups = userQuizGroupRepository.findByUserId(current.getId());
        List groupIds = userQuizGroups.stream().map(UserQuizGroup::getQuizGroupId).collect(Collectors.toList());

        if (status == -1) {
            homeworkQuizzes = findHomeworkQuizzesNotByStatus(tags, makerId, pageable, roles, groupIds);
        } else {
            homeworkQuizzes = findHomeworkQuizzesByStatus(status, tags, makerId, pageable, roles, groupIds);
        }

        return new ResponseEntity<>(utils.format(homeworkQuizzes, pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    public ResponseEntity getHomeworkQuizzesById(@RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                 @RequestParam(value = "id", defaultValue = "0") Long id,
                                                 @RequestParam(value = "status", defaultValue = "-1") int status,
                                                 @Auth User current) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<HomeworkQuiz> homeworkQuizzes;
        if (id == 0) {
            homeworkQuizzes = homeworkQuizRepository.findAllByStatus(status, pageable);
        } else {
            homeworkQuizzes = homeworkQuizRepository.findAllByIdAndStatus(id, status, pageable);
        }

        return new ResponseEntity<>(utils.format(homeworkQuizzes, pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "{quizId}/answerFile", method = RequestMethod.GET)
    public ResponseEntity getHomeworkQuizzes(@PathVariable Long quizId) throws IOException, BusinessException {
        HomeworkQuiz homeworkQuiz = homeworkQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkQuiz with id: %s", quizId)
                ));

        String answerFile = amazonClientService.readFile(homeworkQuiz.getAnswerPath(), "homework-answer");
        return new ResponseEntity<>(answerFile, HttpStatus.OK);
    }

    private Page<HomeworkQuiz> findHomeworkQuizzesNotByStatus(String tags, Long makerId, Pageable pageable, List roles, List groupIds) {
        Page<HomeworkQuiz> homeworkQuizzes = null;

        if ("".equals(tags) && makerId == 0L) {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableIsTrue(pageable);
        } else if ("".equals(tags) && makerId != 0L) {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableIsTrueAndMakerId(makerId, pageable);
        } else if (makerId == 0L && !"".equals(tags)) {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableAndTag(tags.split(","), pageable);
        } else {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableAndTagsAndMakerId(tags.split(","), makerId, pageable);
        }

        return homeworkQuizzes;
    }

    private Page<HomeworkQuiz> findHomeworkQuizzesByStatus(int status, String tags, Long makerId, Pageable pageable, List roles, List groupIds) {
        Page<HomeworkQuiz> homeworkQuizzes = null;
        if ("".equals(tags) && makerId == 0L) {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableIsTrueAndStatus(status, pageable);
        } else if ("".equals(tags) && makerId != 0L) {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableIsTrueAndMakerIdAndStatus(makerId, status, pageable);
        } else if (makerId == 0L && !"".equals(tags)) {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableAndTagAndStatus(tags.split(","), status, pageable);
        } else {
            homeworkQuizzes = homeworkQuizRepository.findAllByIsAvailableAndTagsAndMakerIdAndStatus(tags.split(","), makerId, status, pageable);
        }

        return homeworkQuizzes;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity addHomeWorkQuiz(@RequestBody CreateHomeworkParam body, @Auth User user) {
        HomeworkQuiz homeworkQuiz = new HomeworkQuiz();

        homeworkQuiz.setTemplateRepository("生成中");
        homeworkQuiz.setMakerId(user.getId());
        homeworkQuiz.setTags(utils.formatTags(body.getTags(), user.getId()));
        homeworkQuiz.setDefinitionRepo(body.getDefinitionRepo());
        homeworkQuiz.setHomeworkName(body.getTitle());
        homeworkQuiz.setStackId(body.getStackId());
        homeworkQuiz.setAvailable(true);
        homeworkQuiz.setRemark(body.getRemark());
        homeworkQuiz.setQuizGroupId(body.getQuizGroupId());
        homeworkQuiz = homeworkQuizRepository.save(homeworkQuiz);

        RestTemplate template = new RestTemplate();

        String callbackUrl = quizCenterUrl + "/api/v3/homeworkQuizzes/" + homeworkQuiz.getId() + "/status";

        MultiValueMap data = new LinkedMultiValueMap();
        data.add("github_token", githubToken);
        data.add("git", homeworkQuiz.getDefinitionRepo());
        data.add("callback_url", callbackUrl);

        template.postForObject(addHomeworkQuizJenkinsUrl, data, String.class);
        quizRepository.save(new Quiz(homeworkQuiz.getId(), type));
        Map<String, Object> result = new HashMap<>();
        result.put("uri", "/api/v3/homeworkQuizzes/" + homeworkQuiz.getId());
        result.put("id", homeworkQuiz.getId());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity updateHomeworkQuiz(@PathVariable Long id,
                                             @RequestParam(value = "script", required = false) MultipartFile script,
                                             @RequestParam(value = "answer", required = false) MultipartFile answer,
                                             @RequestParam(value = "readme", required = false) MultipartFile readme,
                                             @RequestParam(value = "answerDescription", required = false) MultipartFile answerDescription,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "result", required = false) String result,
                                             @RequestParam(value = "buildNumber", required = false) Long buildNumber,
                                             @RequestParam(value = "templateRepository", required = false) String templateRepository) throws IOException, BusinessException {

        HomeworkQuiz homeworkQuiz = homeworkQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkQuiz with id: %s", id)
                ));

        log.info(String.format("buildNumber: %s", buildNumber));
        System.out.println(buildNumber);
        if (buildNumber != null) {
            homeworkQuiz.setBuildNumber(buildNumber);
            status = loading + "";
        } else if (saveSuccessStatus == Integer.parseInt(status)) {

            String answerContent = readFileToString(answerDescription);
            String description = readFileToString(readme);

            String answerPath = saveFile(answer, "homework-answer");
            String evaluateScript = saveFile(script, "homework-script");

            homeworkQuiz.setAnswerDescription(answerContent);
            homeworkQuiz.setAnswerPath(answerPath);
            homeworkQuiz.setDescription(description);
            homeworkQuiz.setEvaluateScript(evaluateScript);
            homeworkQuiz.setTemplateRepository(templateRepository);
        }

        homeworkQuiz.setCreateTime(new Date());
        homeworkQuiz.setStatus(JobState.LOADING);
        homeworkQuizRepository.save(homeworkQuiz);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private String saveFile(MultipartFile file, String directory) {

        return amazonClientService.uploadFile(file, directory);
    }

    private String readFileToString(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileContent = IOUtils.toString(inputStream, "utf-8");
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
    public ResponseEntity getAddHomeworkLog(@PathVariable Long id) throws BusinessException {
        HomeworkQuiz homeworkQuiz = homeworkQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkQuiz with id: %s", id)
                ));

        Map results = new HashMap<>();

        if (homeworkQuiz.getBuildNumber() == null) {
            results.put("status", lineUp);
            results.put("logs", "正在排队请稍后...");
        } else {
            int status = homeworkQuiz.getStatus();
            results.put("status", status);
            results.put("logs", homeworkQuiz.getJobMessage());

        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity modifyHomeWorkQuiz(@PathVariable Long id, @RequestBody Map updateHomeworkQuiz, @Auth User user) throws BusinessException {
        HomeworkQuiz homeworkQuiz = homeworkQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkQuiz with id: %s", id)
                ));

        homeworkQuiz.setTemplateRepository("修改中");
        homeworkQuiz.setDefinitionRepo((String) updateHomeworkQuiz.get("definitionRepo"));
        homeworkQuiz.setHomeworkName((String) updateHomeworkQuiz.get("title"));
        homeworkQuiz.setTags(utils.formatTags((List<String>) updateHomeworkQuiz.get("tags"), user.getId()));
        homeworkQuiz.setStackId(((Integer) updateHomeworkQuiz.get("stackId")).longValue());
        homeworkQuiz.setBuildNumber(null);
        homeworkQuiz.setStatus(JobState.LOADING);
        homeworkQuiz.setRemark((String) updateHomeworkQuiz.get("remark"));
        homeworkQuiz.setQuizGroupId(Long.parseLong(updateHomeworkQuiz.get("quizGroupId").toString()));
        homeworkQuizRepository.save(homeworkQuiz);

        RestTemplate template = new RestTemplate();

        String callbackUrl = quizCenterUrl + "/api/v3/homeworkQuizzes/" + id + "/status";

        MultiValueMap data = new LinkedMultiValueMap();
        data.add("github_token", githubToken);
        data.add("git", homeworkQuiz.getDefinitionRepo());
        data.add("callback_url", callbackUrl);

        template.postForObject(addHomeworkQuizJenkinsUrl, data, String.class);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteHomeworkQuiz(@PathVariable Long id) throws BusinessException {
        HomeworkQuiz homeworkQuiz = homeworkQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkQuiz with id: %s", id)
                ));

        homeworkQuiz.setAvailable(false);

        homeworkQuizRepository.save(homeworkQuiz);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}", method = RequestMethod.GET)
    public ResponseEntity getHomeworkWithUserAnswer(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<HomeworkQuiz> homeworkQuizzes = homeworkQuizRepository.findByIdInAndIsAvailableIsTrue(idList);
        List<HomeworkSubmission> quizSubmissions = homeworkSubmissionRepository.findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(idList, assignmentId, studentId);
        List<Map> results = homeworkQuizzes.stream()
                .map(homeworkQuiz -> utils.addUserAnswer(homeworkQuiz, quizSubmissions))
                .collect(Collectors.toList());

        return new ResponseEntity<>(results, HttpStatus.OK);
    }


    @GetMapping(value = "/fuzzy")
    public ResponseEntity fuzzySearch(@RequestParam String type
            , @RequestParam String content
            , @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id");
        Page<HomeworkQuiz> homeworkQuizs = null;
        if (type.equals("remark")) {
            homeworkQuizs = homeworkQuizRepository.findByRemarkLikeAndIsAvailable(
                    "%" + content + "%", true,pageable);
        } else if (type.equals("description")) {
            homeworkQuizs = homeworkQuizRepository.findByDescriptionLikeAndIsAvailable(
                    "%" + content + "%",true, pageable);
        }
        return new ResponseEntity<>(utils.format(homeworkQuizs, pageable), HttpStatus.OK);
    }
}
