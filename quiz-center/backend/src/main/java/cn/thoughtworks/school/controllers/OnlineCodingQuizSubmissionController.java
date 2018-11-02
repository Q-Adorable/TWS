package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.entities.OnlineCodingQuiz;
import cn.thoughtworks.school.entities.OnlineCodingSubmission;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.OnlineCodingQuizRepository;
import cn.thoughtworks.school.repositories.OnlineCodingSubmissionRepository;
import cn.thoughtworks.school.repositories.StackRepository;
import cn.thoughtworks.school.services.AmazonClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
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
@RequestMapping(value = "/api/v3/onlineCodingSubmission")
public class OnlineCodingQuizSubmissionController {
    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private OnlineCodingQuizRepository onlineCodingQuizRepository;

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private OnlineCodingSubmissionRepository onlineCodingSubmissionRepository;

    @Value("${online-coding-quiz-jenkins-url.java.submission}")
    private String submissionOnlineCodingQuizJenkinsJavaUrl;

    @Value("${online-coding-quiz-jenkins-url.java.submission-log}")
    private String submissionOnlineCodingJavaLog;

    @Value("${online-coding-quiz-jenkins-url.ruby.submission}")
    private String submissionOnlineCodingQuizJenkinsRubyUrl;

    @Value("${online-coding-quiz-jenkins-url.ruby.submission-log}")
    private String submissionOnlineCodingRubyLog;

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
    public ResponseEntity judgeOnlineCodingQuiz(@RequestBody HashMap data) throws IOException, BusinessException {
        String userAnswerCode = (String) data.get("userAnswerCode");
        String language = (String) data.get("language");
        String quizName = (String) data.get("name");
        Long assignmentId = Long.valueOf(data.get("assignmentId").toString());
        Long studentId = Long.valueOf(data.get("studentId").toString());

        OnlineCodingQuiz onlineCodingQuiz = onlineCodingQuizRepository.findByOnlineCodingNameAndLanguageAndStatusOrderByCreateTimeDesc(quizName, language, 2).get(0);
        String userAnswerRepo = onlineCodingQuiz.getTemplateRepository();

        OnlineCodingSubmission onlineCodingSubmission = new OnlineCodingSubmission(assignmentId, onlineCodingQuiz.getId(), studentId, userAnswerCode, language, new Date());
        onlineCodingSubmission = onlineCodingSubmissionRepository.save(onlineCodingSubmission);

        Long stackId = onlineCodingQuiz.getStackId();
        Stack stack = stackRepository
                .findById(stackId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown stack with id: %s", stackId)
                ));

        RestTemplate template = new RestTemplate();

        String callbackUrl = quizCenterUrl + "/api/v3/onlineCodingSubmission/callback/" + onlineCodingSubmission.getId();

        FileSystemResource answer = new FileSystemResource(amazonClientService.getFile(onlineCodingQuiz.getAnswerPath(), "onlineCoding-answer"));

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        param.add("answer.zip", answer);
        param.add("user_answer_code", userAnswerCode);
        param.add("user_answer_repo", userAnswerRepo);
        param.add("branch", "master");
        param.add("callback_url", callbackUrl);
        param.add("image", stack.getImage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);

        String submissionOnlineCodingQuizJenkinsUrl = "";
        if (onlineCodingQuiz.getLanguage().equals("java")) {
            submissionOnlineCodingQuizJenkinsUrl = submissionOnlineCodingQuizJenkinsJavaUrl;
        } else if (onlineCodingQuiz.getLanguage().equals("ruby")) {
            submissionOnlineCodingQuizJenkinsUrl = submissionOnlineCodingQuizJenkinsRubyUrl;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(submissionOnlineCodingQuizJenkinsUrl);
        template.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, String.class);

        return new ResponseEntity<>(onlineCodingSubmission, HttpStatus.CREATED);
    }

    // Spring @RequestParam null when sending Parameter in form-data
    @RequestMapping(value = "/callback/{submissionId}", method = RequestMethod.PUT)
    public ResponseEntity callback(@PathVariable Long submissionId,
                                   @RequestParam(value = "result", required = false) MultipartFile result,
                                   @RequestParam(value = "buildNumber", required = false) Long buildNumber,
                                   @RequestParam(value = "status", required = false) Integer status
    ) throws BusinessException {
        OnlineCodingSubmission onlineCodingSubmission = onlineCodingSubmissionRepository
                .findById(submissionId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown OnlineCodingSubmission with id: %s", submissionId)
                ));

        if (Objects.nonNull(status)) {
            onlineCodingSubmission.setStatus(status);
        }
        onlineCodingSubmission.setBuildNumber(buildNumber);
        onlineCodingSubmissionRepository.save(onlineCodingSubmission);
        return new ResponseEntity<>(onlineCodingSubmission, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/{id}/logs", method = RequestMethod.GET)
    public ResponseEntity getSubmissionOnlineCodingLogBySubmissionId(@PathVariable Long id) throws BusinessException {
        OnlineCodingSubmission onlineCodingSubmission = onlineCodingSubmissionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown OnlineCodingSubmission with id: %s", id)
                ));

        Map results = new HashMap();
        if (onlineCodingSubmission.getBuildNumber() == null) {
            results.put("status", lineUp);
            results.put("logs", "正在排队请稍后...");
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        String submissionOnlineCodingLog = "";
        if (onlineCodingSubmission.getAnswerLanguage().equals("java")) {
            submissionOnlineCodingLog = submissionOnlineCodingJavaLog;
        } else if (onlineCodingSubmission.getAnswerLanguage().equals("ruby")) {
            submissionOnlineCodingLog = submissionOnlineCodingRubyLog;
        }

        String getLogUrl = submissionOnlineCodingLog + "/" + onlineCodingSubmission.getBuildNumber() + "/consoleText";
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> data = template.getForEntity(getLogUrl, String.class);
        int status = checkStatus(data.getBody());
        results.put("status", status);
        results.put("logs", data.getBody());

        onlineCodingSubmission.setResult(data.getBody());
        onlineCodingSubmission.setStatus(status);
        onlineCodingSubmissionRepository.save(onlineCodingSubmission);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "users/{userId}/assignments/{assignmentId}/quizzess/{quizId}/logs", method = RequestMethod.GET)
    public ResponseEntity getSubmissionOnlineCodingLogBySubmissionId(@PathVariable Long assignmentId,
                                                                 @PathVariable Long quizId,
                                                                 @PathVariable Long userId) throws BusinessException {
        ArrayList<OnlineCodingSubmission> onlineCodingSubmissions = onlineCodingSubmissionRepository.findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(quizId, assignmentId, userId);
        if (onlineCodingSubmissions.size() > 0) {
            return new ResponseEntity(onlineCodingSubmissions.get(0), HttpStatus.OK);
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
