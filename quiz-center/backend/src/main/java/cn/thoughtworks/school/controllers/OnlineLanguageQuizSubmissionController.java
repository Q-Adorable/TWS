package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.entities.OnlineLanguageQuiz;
import cn.thoughtworks.school.entities.OnlineLanguageSubmission;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.OnlineLanguageQuizRepository;
import cn.thoughtworks.school.repositories.OnlineLanguageSubmissionRepository;
import cn.thoughtworks.school.repositories.StackRepository;
import cn.thoughtworks.school.services.AmazonClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v3/onlineLanguageSubmission")
public class OnlineLanguageQuizSubmissionController {
    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private OnlineLanguageQuizRepository onlineLanguageQuizRepository;

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private OnlineLanguageSubmissionRepository onlineLanguageSubmissionRepository;

    @Value("${online-language-quiz-jenkins-url.java.submission}")
    private String submissionOnlineLanguageQuizJenkinsJavaUrl;

    @Value("${online-language-quiz-jenkins-url.java.submission-log}")
    private String submissionOnlineLanguageJavaLog;

    @Value("${online-language-quiz-jenkins-url.ruby.submission}")
    private String submissionOnlineLanguageQuizJenkinsRubyUrl;

    @Value("${online-language-quiz-jenkins-url.ruby.submission-log}")
    private String submissionOnlineLanguageRubyLog;

    @Value("${quiz-center-url}")
    private String quizCenterUrl;

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
    @Value("${jenkins-callback-status.failure}")
    private int failure;
    @Value("${jenkins-callback-status.success}")
    private int success;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity judgeOnlineLanguageQuiz(@RequestBody HashMap data) throws IOException, BusinessException {
        String userAnswerCode = (String) data.get("userAnswerCode");
        String language = (String) data.get("language");
        Long quizId = ((Integer) data.get("id")).longValue();
        Long assignmentId = Long.valueOf(data.get("assignmentId").toString());
        Long studentId = Long.valueOf(data.get("studentId").toString());

        OnlineLanguageQuiz onlineLanguageQuiz = onlineLanguageQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineLanguageQuiz with id: %s", quizId)
                ));

        OnlineLanguageSubmission onlineLanguageSubmission = new OnlineLanguageSubmission(assignmentId, onlineLanguageQuiz.getId(), studentId, userAnswerCode, language, new Date());
        onlineLanguageSubmission = onlineLanguageSubmissionRepository.save(onlineLanguageSubmission);

        RestTemplate template = new RestTemplate();

        String callbackUrl = quizCenterUrl + "/api/v3/onlineLanguageSubmission/callback/" + onlineLanguageSubmission.getId();

        String testData = onlineLanguageQuiz.getTestData();

        Long stackId = onlineLanguageQuiz.getStackId();
        Stack stack = stackRepository
                .findById(stackId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown stack with id: %s", stackId)
                ));

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        param.add("test_data", testData);
        param.add("user_answer_code", userAnswerCode);
        param.add("callback_url", callbackUrl);
        param.add("image", stack.getImage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);

        String submissionOnlineLanguageQuizJenkinsUrl = "";
        if (onlineLanguageQuiz.getLanguage().equals("java")) {
            submissionOnlineLanguageQuizJenkinsUrl = submissionOnlineLanguageQuizJenkinsJavaUrl;
        } else if (onlineLanguageQuiz.getLanguage().equals("ruby")) {
            submissionOnlineLanguageQuizJenkinsUrl = submissionOnlineLanguageQuizJenkinsRubyUrl;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(submissionOnlineLanguageQuizJenkinsUrl);
        template.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, String.class);

        return new ResponseEntity<>(onlineLanguageSubmission, HttpStatus.CREATED);
    }

    // Spring @RequestParam null when sending Parameter in form-data
    @RequestMapping(value = "/callback/{submissionId}", method = RequestMethod.PUT)
    public ResponseEntity callback(@PathVariable Long submissionId,
                                   @RequestParam(value = "result", required = false) MultipartFile result,
                                   @RequestParam(value = "buildNumber", required = false) Long buildNumber,
                                   @RequestParam(value = "status", required = false) Integer status
    ) throws BusinessException {
        OnlineLanguageSubmission onlineLanguageSubmission = onlineLanguageSubmissionRepository
                .findById(submissionId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineLanguageSubmission with id: %s", submissionId)
                ));

        if (Objects.nonNull(status)) {
            onlineLanguageSubmission.setStatus(status);
        }
        onlineLanguageSubmission.setBuildNumber(buildNumber);
        onlineLanguageSubmissionRepository.save(onlineLanguageSubmission);
        return new ResponseEntity<>(onlineLanguageSubmission, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/{id}/logs", method = RequestMethod.GET)
    public ResponseEntity getSubmissionOnlineLanguageLogBySubmissionId(@PathVariable Long id) throws BusinessException {
        OnlineLanguageSubmission onlineLanguageSubmission = onlineLanguageSubmissionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown onlineLanguageSubmission with id: %s", id)
                ));

        Map results = new HashMap();
        if (onlineLanguageSubmission.getBuildNumber() == null) {
            results.put("status", lineUp);
            results.put("logs", "正在排队请稍后...");
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        String submissionOnlineLanguageLog = "";
        if (onlineLanguageSubmission.getAnswerLanguage().equals("java")) {
            submissionOnlineLanguageLog = submissionOnlineLanguageJavaLog;
        } else if (onlineLanguageSubmission.getAnswerLanguage().equals("ruby")) {
            submissionOnlineLanguageLog = submissionOnlineLanguageRubyLog;
        }

        String getLogUrl = submissionOnlineLanguageLog + "/" + onlineLanguageSubmission.getBuildNumber() + "/consoleText";
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> data = template.getForEntity(getLogUrl, String.class);
        int status = checkStatus(data.getBody());
        results.put("status", status);
        results.put("logs", data.getBody());

        onlineLanguageSubmission.setResult(data.getBody());
        onlineLanguageSubmission.setStatus(status);
        onlineLanguageSubmissionRepository.save(onlineLanguageSubmission);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "users/{userId}/assignments/{assignmentId}/quizzess/{quizId}/logs", method = RequestMethod.GET)
    public ResponseEntity getSubmissionOnlineLanguageLogBySubmissionId(@PathVariable Long assignmentId,
                                                                 @PathVariable Long quizId,
                                                                 @PathVariable Long userId) throws BusinessException {
        ArrayList<OnlineLanguageSubmission> onlineLanguageSubmissions = onlineLanguageSubmissionRepository.findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(quizId, assignmentId, userId);
        if (onlineLanguageSubmissions.size() > 0) {
            return new ResponseEntity(onlineLanguageSubmissions.get(0), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private int checkStatus(String result) {
        if (result.endsWith("FAILURE\n")) {
            return failure;
        }
        if (result.endsWith("SUCCESS\n")) {
            return success;
        }
        return progress;
    }
}
