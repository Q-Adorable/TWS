package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.entities.OnlineLanguageQuiz;
import cn.thoughtworks.school.entities.OnlineLanguageSubmission;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.entities.StackCommand;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.requestParams.SingleLanguageSubmissionParam;
import cn.thoughtworks.school.services.JenkinsService;
import cn.thoughtworks.school.services.SingleLanguageOnlineCodingService;
import cn.thoughtworks.school.services.SingleLanguageOnlineCodingSubmissionService;
import cn.thoughtworks.school.services.StackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/single-language-online-coding-submissions")
public class SingleLanguageOnlineCodingSubmissionController {
    @Autowired
    SingleLanguageOnlineCodingSubmissionService singleLanguageOnlineCodingSubmissionService;
    @Autowired
    SingleLanguageOnlineCodingService singleLanguageOnlineCodingService;
    @Autowired
    StackService stackService;

    @Autowired
    JenkinsService jenkinsService;

    @Value("${jenkins.callbackHost}")
    private String jenkinsCallbackHost;

    @PostMapping
    @Transactional
    public ResponseEntity addSubmission(@RequestBody SingleLanguageSubmissionParam singleLanguageSubmissionParam)
            throws BusinessException, RestClientException {
        OnlineLanguageSubmission onlineLanguageSubmission = singleLanguageOnlineCodingSubmissionService.add(
                singleLanguageSubmissionParam);
        String callbackUrl = String.format("%s/api/single-language-online-coding-submissions/callback/%s",
                jenkinsCallbackHost, onlineLanguageSubmission.getId());
        OnlineLanguageQuiz onlineLanguageQuiz = singleLanguageOnlineCodingService.getOnlineLanguageQuiz(
                singleLanguageSubmissionParam.getId());
        Stack stack = stackService.getStack(onlineLanguageQuiz.getStackId());

        HttpStatus httpStatus = jenkinsService.triggerSingleLanguageCodingSubmissionJob(onlineLanguageQuiz.getTestData()
                , singleLanguageSubmissionParam.getUserAnswerCode()
                , callbackUrl
                , stack.getImage()
                , onlineLanguageSubmission.getAnswerLanguage());
        return new ResponseEntity<>(onlineLanguageSubmission, httpStatus);
    }

    @PostMapping(value = "/callback/{id}")
    public void addSubmissionCallback(@PathVariable Long id,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) String msg,
                                      @RequestParam(required = false) String job_msg) throws BusinessException {
        singleLanguageOnlineCodingSubmissionService.updateCallback(id, status, msg, job_msg);
    }

    @GetMapping(value = "/{userId}/{assignmentId}/{quizId}")
    public ResponseEntity getAllSubmissions(@PathVariable Long userId,
                                            @PathVariable Long assignmentId,
                                            @PathVariable Long quizId,
                                            @RequestParam(required = false, defaultValue = "0") Integer page,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<OnlineLanguageSubmission> list = singleLanguageOnlineCodingSubmissionService.
                getList(userId, assignmentId, quizId, page, size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getSubmission(@PathVariable Long id) throws BusinessException {
        OnlineLanguageSubmission onlineLanguageSubmission = singleLanguageOnlineCodingSubmissionService.get(id);
        return new ResponseEntity<>(onlineLanguageSubmission, HttpStatus.OK);
    }

}
