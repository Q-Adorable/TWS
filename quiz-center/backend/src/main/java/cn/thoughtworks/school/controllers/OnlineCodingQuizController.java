package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.OnlineCodingQuiz;
import cn.thoughtworks.school.entities.OnlineCodingSubmission;
import cn.thoughtworks.school.entities.Quiz;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.OnlineCodingQuizRepository;
import cn.thoughtworks.school.repositories.OnlineCodingSubmissionRepository;
import cn.thoughtworks.school.repositories.QuizRepository;
import cn.thoughtworks.school.repositories.StackRepository;
import cn.thoughtworks.school.services.AmazonClientService;
import cn.thoughtworks.school.services.Utils;
import cn.thoughtworks.school.services.UserCenterService;
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
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/v3/onlineCodingQuizzes")
public class OnlineCodingQuizController {

    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private OnlineCodingQuizRepository onlineCodingQuizRepository;

    @Autowired
    private StackRepository stackRepository;
    @Autowired
    private QuizRepository quizRepository;

    @Value("${online-coding-quiz-jenkins-url.add}")
    private String addOnlineCodingQuizJenkinsUrl;

    @Value("${online-coding-quiz-jenkins-url.add-log}")
    private String addOnlineCodingLog;

    @Value("${quiz-center-url}")
    private String quizCenterUrl;
    @Value("${online-coding-type}")
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
    private OnlineCodingSubmissionRepository onlineCodingSubmissionRepository;

    private String githubToken = "ca56d5b5467bc58ed784d14483bc233e87291fd0";

    @Autowired
    OnlineCodingQuizController(AmazonClientService amazonClientService, ResourceLoader resourceLoader) {
        this.amazonClientService = amazonClientService;
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.GET)
    public ResponseEntity getCoding(@PathVariable String ids, @RequestParam(value = "status", defaultValue = "") String status) throws BusinessException {

        List<Long> idList = Arrays.stream(ids.split(","))
                .map(id -> Long.parseLong(id))
                .collect(Collectors.toList());

        List<OnlineCodingQuiz> onlineCodingQuizzes = onlineCodingQuizRepository.findByIdInAndIsAvailableIsTrue(idList);

        if (status.equals("")) {
            if (onlineCodingQuizzes.size() == 0) {
                throw new BusinessException("该编程题不存在");
            }
            List<Map> users = userCenterService.getUsersByIds(onlineCodingQuizzes.get(0).getMakerId().toString());
            return new ResponseEntity<>(utils.formatEntry(onlineCodingQuizzes.get(0), users), HttpStatus.OK);
        }
        return new ResponseEntity<>(utils.formatList(onlineCodingQuizzes), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getOnlineCodingQuizzes(@RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                 @RequestParam(value = "tags", defaultValue = "") String tags,
                                                 @RequestParam(value = "makerId", defaultValue = "0") Long makerId,
                                                 @RequestParam(value = "status", defaultValue = "-1") int status) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<OnlineCodingQuiz> onlineCodingQuizzes = null;
        if (status == -1) {
            onlineCodingQuizzes = findOnlineCodingQuizzesNotByStatus(tags, makerId, pageable);
        } else {
            onlineCodingQuizzes = findOnlineCodingQuizzesByStatus(status, tags, makerId, pageable);
        }
        return new ResponseEntity<>(utils.format(onlineCodingQuizzes, pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    public ResponseEntity getOnlineCodingQuizzesById(@RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                     @RequestParam(value = "id", defaultValue = "0") Long id,
                                                     @RequestParam(value = "status", defaultValue = "-1") int status) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<OnlineCodingQuiz> onlineCodingQuizzes;
        if (id == 0) {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByStatus(status, pageable);
        } else {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIdAndStatus(id, status, pageable);
        }
        return new ResponseEntity<>(utils.format(onlineCodingQuizzes, pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}/answerFile", method = RequestMethod.GET)
    public ResponseEntity getOnlineCodingQuizzes(@PathVariable Long id) throws IOException, BusinessException {
        OnlineCodingQuiz onlineCodingQuiz = onlineCodingQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineCodingQuiz with id: %s", id)
                ));
        String answerFile = amazonClientService.readFile(onlineCodingQuiz.getAnswerPath(), "onlineCoding-answer");
        return new ResponseEntity<>(answerFile, HttpStatus.OK);
    }

    private Page<OnlineCodingQuiz> findOnlineCodingQuizzesNotByStatus(String tags, Long makerId, Pageable pageable) {
        Page<OnlineCodingQuiz> onlineCodingQuizzes = null;

        if ("".equals(tags) && makerId == 0L) {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableIsTrue(pageable);
        } else if ("".equals(tags) && makerId != 0L) {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableIsTrueAndMakerId(makerId, pageable);
        } else if (makerId == 0L && !"".equals(tags)) {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableAndTag(tags.split(","), pageable);
        } else {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableAndTagsAndMakerId(tags.split(","), makerId, pageable);
        }

        return onlineCodingQuizzes;
    }

    private Page<OnlineCodingQuiz> findOnlineCodingQuizzesByStatus(int status, String tags, Long makerId, Pageable pageable) {
        Page<OnlineCodingQuiz> onlineCodingQuizzes = null;
        if ("".equals(tags) && makerId == 0L) {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableIsTrueAndStatus(status, pageable);
        } else if ("".equals(tags) && makerId != 0L) {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableIsTrueAndMakerIdAndStatus(makerId, status, pageable);
        } else if (makerId == 0L && !"".equals(tags)) {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableAndTagAndStatus(tags.split(","), status, pageable);
        } else {
            onlineCodingQuizzes = onlineCodingQuizRepository.findAllByIsAvailableAndTagsAndMakerIdAndStatus(tags.split(","), makerId, status, pageable);
        }

        return onlineCodingQuizzes;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity addOnlineCodingQuiz(@RequestBody Map body, @Auth User user) {
        OnlineCodingQuiz onlineCodingQuiz = new OnlineCodingQuiz();

        onlineCodingQuiz.setTemplateRepository("生成中");
        onlineCodingQuiz.setMakerId(user.getId());
        onlineCodingQuiz.setTags(utils.formatTags((List<String>) body.get("tags"), user.getId()));
        onlineCodingQuiz.setDefinitionRepo((String) body.get("definitionRepo"));
        onlineCodingQuiz.setLanguage((String) body.get("language"));
        onlineCodingQuiz.setOnlineCodingName((String) body.get("title"));
        onlineCodingQuiz.setStackId(((Integer) body.get("stackId")).longValue());
        onlineCodingQuiz.setAvailable(true);
        onlineCodingQuiz.setRemark((String) body.get("remark"));

        onlineCodingQuiz = onlineCodingQuizRepository.save(onlineCodingQuiz);

        RestTemplate template = new RestTemplate();

        String callbackUrl = quizCenterUrl + "/api/v3/onlineCodingQuizzes/" + onlineCodingQuiz.getId() + "/status";

        MultiValueMap data = new LinkedMultiValueMap();
        data.add("github_token", githubToken);
        data.add("git", onlineCodingQuiz.getDefinitionRepo());
        data.add("callback_url", callbackUrl);

        template.postForObject(addOnlineCodingQuizJenkinsUrl, data, String.class);
        quizRepository.save(new Quiz(onlineCodingQuiz.getId(), type));
        Map<String, Object> result = new HashMap<>();
        result.put("uri", "/api/v3/onlineCodingQuizzes/" + onlineCodingQuiz.getId());
        result.put("id", onlineCodingQuiz.getId());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity updateOnlineCodingQuiz(@PathVariable Long id,
                                                 @RequestParam(value = "script", required = false) MultipartFile script,
                                                 @RequestParam(value = "answer", required = false) MultipartFile answer,
                                                 @RequestParam(value = "readme", required = false) MultipartFile readme,
                                                 @RequestParam(value = "answerDescription", required = false) MultipartFile answerDescription,
                                                 @RequestParam(value = "status", required = false) String status,
                                                 @RequestParam(value = "result", required = false) String result,
                                                 @RequestParam(value = "buildNumber", required = false) Long buildNumber,
                                                 @RequestParam(value = "templateRepository", required = false) String templateRepository) throws IOException, BusinessException {

        OnlineCodingQuiz onlineCodingQuiz = onlineCodingQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineCodingQuiz with id: %s", id)
                ));

        if (buildNumber != null) {
            onlineCodingQuiz.setBuildNumber(buildNumber);
            status = loading + "";
        } else if (saveSuccessStatus == Integer.parseInt(status)) {

            String answerContent = utils.readFileToString(answerDescription);
            String description = utils.readFileToString(readme);

            String answerPath = saveFile(answer, "onlineCoding-answer");
            String evaluateScript = saveFile(script, "onlineCoding-script");

            onlineCodingQuiz.setAnswerDescription(answerContent);
            onlineCodingQuiz.setAnswerPath(answerPath);
            onlineCodingQuiz.setDescription(description);
            onlineCodingQuiz.setEvaluateScript(evaluateScript);
            onlineCodingQuiz.setTemplateRepository(templateRepository);
        }


        onlineCodingQuiz.setCreateTime(((Long) new Date().getTime()).intValue());
        onlineCodingQuiz.setStatus(Integer.parseInt(status));

        onlineCodingQuizRepository.save(onlineCodingQuiz);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private String saveFile(MultipartFile file, String directory) {

        return amazonClientService.uploadFile(file, directory);
    }


    @RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
    public ResponseEntity getAddOnlineCodingLog(@PathVariable Long id) throws BusinessException {
        OnlineCodingQuiz onlineCodingQuiz = onlineCodingQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineCodingQuiz with id: %s", id)
                ));

        Map results = new HashMap();

        if (onlineCodingQuiz.getBuildNumber() == null) {
            results.put("status", lineUp);
            results.put("logs", "正在排队请稍后...");
        } else {
            String getLogUrl = addOnlineCodingLog + "/" + onlineCodingQuiz.getBuildNumber() + "/consoleText";
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> data = template.getForEntity(getLogUrl, String.class);

            results.put("status", onlineCodingQuiz.getStatus());
            results.put("logs", data.getBody());
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity modifyHomeWorkQuiz(@PathVariable Long id, @RequestBody Map updateOnlineCodingQuiz, @Auth User user) throws BusinessException {
        OnlineCodingQuiz onlineCodingQuiz = onlineCodingQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineCodingQuiz with id: %s", id)
                ));

        onlineCodingQuiz.setTemplateRepository("修改中");
        onlineCodingQuiz.setDefinitionRepo((String) updateOnlineCodingQuiz.get("definitionRepo"));
        onlineCodingQuiz.setLanguage((String) updateOnlineCodingQuiz.get("language"));
        onlineCodingQuiz.setOnlineCodingName((String) updateOnlineCodingQuiz.get("title"));
        onlineCodingQuiz.setTags(utils.formatTags((List<String>) updateOnlineCodingQuiz.get("tags"), user.getId()));
        onlineCodingQuiz.setStackId(((Integer) updateOnlineCodingQuiz.get("stackId")).longValue());
        onlineCodingQuiz.setBuildNumber(null);
        onlineCodingQuiz.setStatus(lineUp);
        onlineCodingQuiz.setRemark((String) updateOnlineCodingQuiz.get("remark"));
        onlineCodingQuiz.setQuizGroupId(Long.parseLong(updateOnlineCodingQuiz.get("quizGroupId").toString()));
        onlineCodingQuizRepository.save(onlineCodingQuiz);

        RestTemplate template = new RestTemplate();

        String callbackUrl = quizCenterUrl + "/api/v3/onlineCodingQuizzes/" + id + "/status";

        MultiValueMap data = new LinkedMultiValueMap();
        data.add("github_token", githubToken);
        data.add("git", onlineCodingQuiz.getDefinitionRepo());
        data.add("callback_url", callbackUrl);

        template.postForObject(addOnlineCodingQuizJenkinsUrl, data, String.class);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOnlineCodingQuiz(@PathVariable Long id) throws BusinessException {
        OnlineCodingQuiz onlineCodingQuiz = onlineCodingQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineCodingQuiz with id: %s", id)
                ));

        onlineCodingQuiz.setAvailable(false);

        onlineCodingQuizRepository.save(onlineCodingQuiz);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}", method = RequestMethod.GET)
    public ResponseEntity getOnlineCodingWithUserAnswer(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<OnlineCodingQuiz> onlineCodingQuizzes = onlineCodingQuizRepository.findByIdInAndIsAvailableIsTrue(idList);
        List<OnlineCodingSubmission> quizSubmissions = onlineCodingSubmissionRepository.findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(idList, assignmentId, studentId);

        List<Map> results = onlineCodingQuizzes.stream()
                .map(onlineCodingQuiz -> utils.addUserAnswer(onlineCodingQuiz, quizSubmissions))
                .collect(Collectors.toList());

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
