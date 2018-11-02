package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.entities.HomeworkQuiz;
import cn.thoughtworks.school.entities.HomeworkSubmission;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.requestParams.HomeworkSubmissionParam;
import cn.thoughtworks.school.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/single-stack-programming-quiz-submissions")

public class SingleStackProgrammingQuizSubmissionController {

    @Value("${jenkins.callbackHost}")
    private String jenkinsCallbackHost;
    @Autowired
    SingleStackProgrammingSubmissionService singleStackProgrammingSubmissionService;
    @Autowired
    HomeworkQuizService homeworkQuizService;
    @Autowired
    StackService stackService;
    @Autowired
    JenkinsService jenkinsService;
    @Autowired
    AmazonClientService amazonClientService;

    // 提交某个题目的答案
    // 1.存入数据库
    // 2.向jenkins发请求
    @PostMapping
    @Transactional
    public ResponseEntity createSubmission(@RequestBody HomeworkSubmissionParam param) throws BusinessException, IOException {
        HomeworkSubmission homeworksubmission = singleStackProgrammingSubmissionService.save(param);
        HomeworkQuiz homeworkQuiz = homeworkQuizService.getHomeworkQuiz(param.getQuizId());
        Stack stack = stackService.getStack(homeworkQuiz.getStackId());
        String callbackUrl = String.format("%s/api/single-stack-programming-quiz-submissions/%s",
                jenkinsCallbackHost, homeworksubmission.getId());
        String answerUrl = String.format("%s/api/single-stack-programming-quiz/%s/answerFile",
                jenkinsCallbackHost, homeworkQuiz.getId());
        HttpStatus httpStatus = jenkinsService.triggerSingleStackProgrammingQuizSubmissionJob(
                param.getUserAnswerRepo(),
                param.getBranch(),
                callbackUrl,
                stack.getImage(),
                answerUrl
        );
        if (httpStatus.value() != 201) {
            throw new BusinessException("jenkins server error!");
        }
        return new ResponseEntity<>(homeworksubmission, httpStatus);
    }

    //jenkins回调
    @PostMapping(value = "/{id}")
    public ResponseEntity updateSubmission(@PathVariable Long id
            , @RequestParam(required = false) Integer status
            , @RequestParam(required = false) String msg
            , @RequestParam(required = false) String job_msg
            , @RequestParam(required = false) String buildNumber) {
        HomeworkSubmission submission = singleStackProgrammingSubmissionService.getSubmission(id);
        submission.setStatus(status);
        submission.setBuildNumber(Long.parseLong(buildNumber));
        submission.setResult(msg + "\n" + job_msg.trim());
        singleStackProgrammingSubmissionService.save(submission);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 轮询接口
     * unused
     * @param id HomeworkSubmission id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity getSubmission(@PathVariable Long id) {
        HomeworkSubmission submission = singleStackProgrammingSubmissionService.getSubmission(id);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    /**
     * 得到全部HomeworkSubmission
     * unused
     * @param userId       用户id
     * @param assignmentId assignment id
     * @param quizId       问题 id
     * @param page         分页 page
     * @param size         分页 size
     * @return
     */
    @GetMapping(value = "/{userId}/{assignmentId}/{quizId}")
    public ResponseEntity getAllSubmission(@PathVariable Long userId
            , @PathVariable Long assignmentId
            , @PathVariable Long quizId
            , @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "size", defaultValue = "10") int size) {
        List<HomeworkSubmission> list = singleStackProgrammingSubmissionService.getAllSubmission(
                userId, assignmentId, quizId, page, size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
