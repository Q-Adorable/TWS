package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.*;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.OnlineLanguageQuizRepository;
import cn.thoughtworks.school.repositories.OnlineLanguageSubmissionRepository;
import cn.thoughtworks.school.repositories.QuizRepository;
import cn.thoughtworks.school.repositories.StackRepository;
import cn.thoughtworks.school.services.*;
import org.apache.commons.io.FileUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/v3/onlineLanguageQuizzes")
public class OnlineLanguageQuizController {

    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private OnlineLanguageQuizRepository onlineLanguageQuizRepository;

    @Autowired
    private StackRepository stackRepository;
    @Autowired
    private QuizRepository quizRepository;

    @Value("${quiz-center-url}")
    private String quizCenterUrl;
    @Value("${online-language-type}")
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
    private OnlineLanguageSubmissionRepository onlineLanguageSubmissionRepository;

    @Autowired
    private SingleLanguageOnlineCodingService singleLanguageOnlineCodingService;

    @Autowired
    private StackCommandService stackCommandService;

    @Autowired
    OnlineLanguageQuizController(AmazonClientService amazonClientService, ResourceLoader resourceLoader) {
        this.amazonClientService = amazonClientService;
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.GET)
    public ResponseEntity getLanguage(@PathVariable String ids, @RequestParam(value = "status", defaultValue = "") String status) throws BusinessException {

        List<Long> idList = Arrays.stream(ids.split(","))
                .map(id -> Long.parseLong(id))
                .collect(Collectors.toList());

        List<OnlineLanguageQuiz> onlineLanguageQuizzes = onlineLanguageQuizRepository.findByIdInAndIsAvailableIsTrue(idList);

        if (status.equals("")) {
            if (onlineLanguageQuizzes.size() == 0) {
                throw new BusinessException("该编程题不存在");
            }
            List<Map> users = userCenterService.getUsersByIds(onlineLanguageQuizzes.get(0).getMakerId().toString());
            return new ResponseEntity<>(utils.formatEntry(onlineLanguageQuizzes.get(0), users), HttpStatus.OK);
        }
        return new ResponseEntity<>(utils.formatList(onlineLanguageQuizzes), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getOnlineLanguageQuizzes(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "tags", defaultValue = "") String tags,
                                                   @RequestParam(value = "makerId", defaultValue = "0") Long makerId,
                                                   @RequestParam(value = "status", defaultValue = "-1") int status) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<OnlineLanguageQuiz> onlineLanguageQuizzes = null;
        if (status == -1) {
            onlineLanguageQuizzes = findOnlineLanguageQuizzesNotByStatus(tags, makerId, pageable);
        } else {
            onlineLanguageQuizzes = findOnlineLanguageQuizzesByStatus(status, tags, makerId, pageable);
        }
        return new ResponseEntity<>(utils.format(onlineLanguageQuizzes, pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    public ResponseEntity getOnlineLanguageQuizzesById(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                       @RequestParam(value = "id", defaultValue = "0") Long id,
                                                       @RequestParam(value = "status", defaultValue = "-1") int status) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<OnlineLanguageQuiz> onlineLanguageQuizzes;
        if (id == 0) {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByStatus(status, pageable);
        } else {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIdAndStatus(id, status, pageable);
        }
        return new ResponseEntity<>(utils.format(onlineLanguageQuizzes, pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "{quizId}/answerFile", method = RequestMethod.GET)
    public ResponseEntity getOnlineLanguageQuizzes(@PathVariable Long quizId) throws IOException, BusinessException {
        OnlineLanguageQuiz onlineLanguageQuiz = onlineLanguageQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineLanguageQuiz with id: %s", quizId)
                ));
        return new ResponseEntity<>(onlineLanguageQuiz.getAnswer(), HttpStatus.OK);
    }

    private Page<OnlineLanguageQuiz> findOnlineLanguageQuizzesNotByStatus(String tags, Long makerId, Pageable pageable) {
        Page<OnlineLanguageQuiz> onlineLanguageQuizzes = null;

        if ("".equals(tags) && makerId == 0L) {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableIsTrue(pageable);
        } else if ("".equals(tags) && makerId != 0L) {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableIsTrueAndMakerId(makerId, pageable);
        } else if (makerId == 0L && !"".equals(tags)) {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableAndTag(tags.split(","), pageable);
        } else {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableAndTagsAndMakerId(tags.split(","), makerId, pageable);
        }

        return onlineLanguageQuizzes;
    }

    private Page<OnlineLanguageQuiz> findOnlineLanguageQuizzesByStatus(int status, String tags, Long makerId, Pageable pageable) {
        Page<OnlineLanguageQuiz> onlineLanguageQuizzes = null;
        if ("".equals(tags) && makerId == 0L) {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableIsTrueAndStatus(status, pageable);
        } else if ("".equals(tags) && makerId != 0L) {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableIsTrueAndMakerIdAndStatus(makerId, status, pageable);
        } else if (makerId == 0L && !"".equals(tags)) {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableAndTagAndStatus(tags.split(","), status, pageable);
        } else {
            onlineLanguageQuizzes = onlineLanguageQuizRepository.findAllByIsAvailableAndTagsAndMakerIdAndStatus(tags.split(","), makerId, status, pageable);
        }

        return onlineLanguageQuizzes;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity addOnlineLanguageQuiz(@RequestBody Map body, @Auth User user) throws ValidationException, IOException, JSONException {
        // 校验json，抛出ValidationException异常
        singleLanguageOnlineCodingService.Jsonerification((String) body.get("testData"));

        OnlineLanguageQuiz onlineLanguageQuiz = new OnlineLanguageQuiz();
        onlineLanguageQuiz.setMakerId(user.getId());
        onlineLanguageQuiz.setTags(utils.formatTags((List<String>) body.get("tags"), user.getId()));
        onlineLanguageQuiz.setLanguage((String) body.get("language"));
        onlineLanguageQuiz.setOnlineLanguageName((String) body.get("title"));
        onlineLanguageQuiz.setDescription((String) body.get("description"));
        onlineLanguageQuiz.setTestData((String) body.get("testData"));
        onlineLanguageQuiz.setInitCode((String) body.get("initCode"));
        onlineLanguageQuiz.setAnswer((String) body.get("answer"));
        onlineLanguageQuiz.setAnswerDescription((String) body.get("answerDescription"));
        onlineLanguageQuiz.setStackId(((Integer) body.get("stackId")).longValue());
        onlineLanguageQuiz.setAvailable(true);
        onlineLanguageQuiz.setRemark((String) body.get("remark"));
        onlineLanguageQuiz.setStatus(2); // status: SUCCESS

        onlineLanguageQuiz = onlineLanguageQuizRepository.save(onlineLanguageQuiz);

        quizRepository.save(new Quiz(onlineLanguageQuiz.getId(), type));
        Map<String, Object> result = new HashMap<>();
        result.put("uri", "/api/v3/onlineLanguageQuizzes/" + onlineLanguageQuiz.getId());
        result.put("id", onlineLanguageQuiz.getId());
        result.put("status", onlineLanguageQuiz.getStatus());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    private String saveFile(MultipartFile file, String directory) {

        return amazonClientService.uploadFile(file, directory);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity modifyHomeWorkQuiz(@PathVariable Long id, @RequestBody Map updateOnlineLanguageQuiz, @Auth User user) throws BusinessException {
        OnlineLanguageQuiz onlineLanguageQuiz = onlineLanguageQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineLanguageQuiz with id: %s", id)
                ));

        onlineLanguageQuiz.setLanguage((String) updateOnlineLanguageQuiz.get("language"));
        onlineLanguageQuiz.setOnlineLanguageName((String) updateOnlineLanguageQuiz.get("title"));
        onlineLanguageQuiz.setDescription((String) updateOnlineLanguageQuiz.get("description"));
        onlineLanguageQuiz.setTestData((String) updateOnlineLanguageQuiz.get("testData"));
        onlineLanguageQuiz.setInitCode((String) updateOnlineLanguageQuiz.get("initCode"));
        onlineLanguageQuiz.setAnswer((String) updateOnlineLanguageQuiz.get("answer"));
        onlineLanguageQuiz.setAnswerDescription((String) updateOnlineLanguageQuiz.get("answerDescription"));
        onlineLanguageQuiz.setStackId(((Integer) updateOnlineLanguageQuiz.get("stackId")).longValue());
        onlineLanguageQuiz.setTags(utils.formatTags((List<String>) updateOnlineLanguageQuiz.get("tags"), user.getId()));
        onlineLanguageQuiz.setRemark((String) updateOnlineLanguageQuiz.get("remark"));

        onlineLanguageQuizRepository.save(onlineLanguageQuiz);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOnlineLanguageQuiz(@PathVariable Long id) throws BusinessException {
        OnlineLanguageQuiz onlineLanguageQuiz = onlineLanguageQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineLanguageQuiz with id: %s", id)
                ));

        onlineLanguageQuiz.setAvailable(false);

        onlineLanguageQuizRepository.save(onlineLanguageQuiz);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}", method = RequestMethod.GET)
    public ResponseEntity getOnlineLanguageWithUserAnswer(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<OnlineLanguageQuiz> onlineLanguageQuizzes = onlineLanguageQuizRepository.findByIdInAndIsAvailableIsTrue(idList);
        List<OnlineLanguageSubmission> quizSubmissions = onlineLanguageSubmissionRepository.findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(idList, assignmentId, studentId);

        List<Map> results = onlineLanguageQuizzes.stream()
                .map(onlineLanguageQuiz -> utils.addUserAnswer(onlineLanguageQuiz, quizSubmissions))
                .collect(Collectors.toList());

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(value = "/template/{language}")
    public ResponseEntity getLanguageTemplate(@PathVariable String language) throws BusinessException {

        StackCommand stackCommand = stackCommandService.getStackCommandByName(language);
        if (stackCommand == null) {
            throw new BusinessException(String.format("can not find StackCommand by %s", language));
        }
        Map<String, String> map = new HashMap<>();
        map.put("initCode", stackCommand.getTemplateCode());
        map.put("testcase", stackCommand.getTestcase());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping(value = "/languages")
    public ResponseEntity getAllLanguage() {
        List<StackCommand> allStackCommand = stackCommandService.getAllStackCommand();
        List<String> result = allStackCommand.stream().map(StackCommand::getName).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/fuzzy")
    public ResponseEntity fuzzySearch(@RequestParam String type
            , @RequestParam String content
            , @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.Direction.DESC, "id");
        Page<OnlineLanguageQuiz> onlineLanguageQuizzes = null;
        if (type.equals("remark")) {
            onlineLanguageQuizzes = singleLanguageOnlineCodingService.getRemarkLike("%"+content+"%", pageable);
        } else if (type.equals("description")) {
            onlineLanguageQuizzes = singleLanguageOnlineCodingService.getDescriptionLike("%"+content+"%",pageable);
        }
        return new ResponseEntity<>(utils.format(onlineLanguageQuizzes, pageable), HttpStatus.OK);
    }
}
