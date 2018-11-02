package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.entities.HomeworkQuiz;
import cn.thoughtworks.school.entities.HomeworkSubmission;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.HomeworkQuizRepository;
import cn.thoughtworks.school.repositories.HomeworkSubmissionRepository;
import cn.thoughtworks.school.repositories.StackRepository;
import cn.thoughtworks.school.requestParams.HomeworkSubmissionParam;
import cn.thoughtworks.school.services.AmazonClientService;
import cn.thoughtworks.school.services.FileLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v3/homeworkSubmission")
public class HomeworkSubmissionController {

    private final Logger logger = LoggerFactory.getLogger(StackController.class);

    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private FileLoadService fileLoadService;

    @Autowired
    private HomeworkQuizRepository homeworkQuizRepository;

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private HomeworkSubmissionRepository homeworkSubmissionRepository;

    @Value("${homework-quiz-jenkins-url.submission}")
    private String submissionHomeworkQuizJenkinsUrl;

//    private String submissionHomeworkQuizJenkinsUrl = "http://10.206.22.167:8088/job/HOMEWORK_SCORING/buildWithParameters";

    @Value("${homework-quiz-jenkins-url.submission-log}")
    private String submissionHomeworkLog;

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
    public ResponseEntity judgeHomeworkQuiz(@Valid @RequestBody HomeworkSubmissionParam data) throws BusinessException {
        logger.info(String.format("POST %s %s", "/api/v3/homeworkSubmission", data));

        String userAnswerRepo = data.getUserAnswerRepo();
        String branch = data.getBranch();
        Long quizId = data.getQuizId();
        Long assignmentId = data.getAssignmentId();
        Long studentId = data.getStudentId();

        HomeworkSubmission homeworkSubmission = new HomeworkSubmission(assignmentId, quizId, studentId, userAnswerRepo, branch, new Date());
        homeworkSubmission = homeworkSubmissionRepository.save(homeworkSubmission);

        HomeworkQuiz homeworkQuiz = homeworkQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkQuiz with id: %s", quizId)
                ));

        Long stackId = homeworkQuiz.getStackId();
        Stack stack = stackRepository
                .findById(stackId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown tag with id: %s", stackId)
                ));

        RestTemplate template = new RestTemplate();

        String callbackUrl = quizCenterUrl + "/api/v3/homeworkSubmission/callback/" + homeworkSubmission.getId();

        FileSystemResource answer = new FileSystemResource(amazonClientService.getFile(homeworkQuiz.getAnswerPath(), "homework-answer"));

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        param.add("answer.zip", answer);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(submissionHomeworkQuizJenkinsUrl)
                .queryParam("user_answer_repo", userAnswerRepo)
                .queryParam("branch", branch)
                .queryParam("callback_url", callbackUrl)
                .queryParam("image", stack.getImage());

        template.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, String.class);

        return new ResponseEntity<>(homeworkSubmission, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/callback/{submissionId}", method = RequestMethod.POST)
    public ResponseEntity callback(@PathVariable Long submissionId,
                                   @RequestParam(value = "buildNumber", required = false) Long buildNumber,
                                   @RequestParam(value = "status", required = false) Integer status,
                                   @RequestParam(value = "commitId", required = false) String commitId,
                                   @RequestParam(value = "result", required = false) MultipartFile result
    ) {
        HomeworkSubmission homeworkSubmission = homeworkSubmissionRepository.findById(submissionId).get();

        if (buildNumber != null && homeworkSubmission.getBuildNumber() == null) {
            homeworkSubmission.setBuildNumber(buildNumber);
        }

        if (status != null) {
            homeworkSubmission.setStatus(status);
        }

        if (commitId != null) {
            homeworkSubmission.setCommitId(commitId);
        }

        if (result != null) {
            // todo 数据库中的字符串长度为64k
            String resultStr = fileLoadService.read(result);
            logger.info(String.format("uploaded file length is: %s", resultStr));
            homeworkSubmission.setResult(resultStr);
        }

        homeworkSubmissionRepository.save(homeworkSubmission);
        return new ResponseEntity<>("Update Successful", HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}/logs", method = RequestMethod.GET)
    public ResponseEntity getSubmissionHomeworkLogBySubmissionId(@PathVariable Long id) throws BusinessException {
        HomeworkSubmission homeworkSubmission = homeworkSubmissionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkSubmission with id: %s", id)
                ));

        Map results = new HashMap();
        if (homeworkSubmission.getBuildNumber() == null) {
            results.put("status", lineUp);
            results.put("logs", "正在排队请稍后...");
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

//        String getLogUrl = submissionHomeworkLog + "/" + homeworkSubmission.getBuildNumber() + "/consoleText";
//        RestTemplate template = new RestTemplate();
//        ResponseEntity<String> data = template.getForEntity(getLogUrl, String.class);
//        int status = checkStatus(data.getBody());
        int status = homeworkSubmission.getStatus();
        String result = homeworkSubmission.getResult();
        results.put("status", status);
        results.put("logs", result);
//        results.put("status", status);
//        results.put("logs", data.getBody());

//        homeworkSubmission.setResult(data.getBody());
        homeworkSubmission.setResult(result);
        homeworkSubmission.setStatus(status);
        homeworkSubmissionRepository.save(homeworkSubmission);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "users/{userId}/assignments/{assignmentId}/quizzess/{quizId}/logs", method = RequestMethod.GET)
    public ResponseEntity getSubmissionHomeworkLogBySubmissionId(@PathVariable Long assignmentId,
                                                                 @PathVariable Long quizId,
                                                                 @PathVariable Long userId) throws BusinessException {
        ArrayList<HomeworkSubmission> homeworkSubmissions = homeworkSubmissionRepository.findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(quizId, assignmentId, userId);
        if (homeworkSubmissions.size() > 0) {
            return new ResponseEntity(homeworkSubmissions.get(0), HttpStatus.OK);
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

    @RequestMapping(value = "/oldAssignments/{oldAssignmentId}/quizzes/{quizId}/newAssignments/{newAssignmentId}", method = RequestMethod.GET)
    public ResponseEntity homeworkDataMigration(@PathVariable Long oldAssignmentId, @PathVariable Long quizId, @PathVariable Long newAssignmentId) throws BusinessException {
        List<HomeworkSubmission> homeworkSubmissions = homeworkSubmissionRepository.findAllByAssignmentIdAndQuizId(oldAssignmentId, quizId);
        if (homeworkSubmissions.size() != 0) {
            for (HomeworkSubmission homeworkSubmission : homeworkSubmissions) {
                homeworkSubmission.setAssignmentId(newAssignmentId);
                homeworkSubmissionRepository.save(homeworkSubmission);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
