package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.LogicQuiz;
import cn.thoughtworks.school.entities.TWLogicQuizSubmission;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.LogicQuizRepository;
import cn.thoughtworks.school.repositories.TWLogicQuizSubmissionRepository;
import cn.thoughtworks.school.requestParams.SubmitUserAnswerParam;
import cn.thoughtworks.school.services.TWLogicQuizSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(value = "/api/tw-logic-quiz-submission")
public class TWLogicQuizSubmissionController {

    @Autowired
    TWLogicQuizSubmissionService submissionService;
    @Autowired
    TWLogicQuizSubmissionRepository submissionRepository;
    @Autowired
    LogicQuizRepository logicQuizRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Transactional
    public TWLogicQuizSubmission getSubmission(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("quizId") Long quizId,
            @Auth User current) throws BusinessException {
        return submissionService.findByAssignmentAndUserId(assignmentId, quizId, current.getId());
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @Transactional
    public TWLogicQuizSubmission getSubmissionSummary(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("quizId") Long quizId,
            @Auth User current) throws BusinessException {
        return submissionService.getSummary(assignmentId, quizId, current.getId());
    }

    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity submitUserAnswer(@Auth User current, @RequestBody SubmitUserAnswerParam param) throws BusinessException {
        param.setUserId(current.getId());
        verifySubmitTime(param);
        submissionService.submitUserAnswer(param);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    private void verifySubmitTime(SubmitUserAnswerParam param) throws BusinessException {
        TWLogicQuizSubmission submission = submissionRepository
                .findTopByAssignmentIdAndUserId(param.getAssignmentId(), param.getUserId())
                .orElseThrow(() -> {
                    String message = String.format("TWLogicQuizSubmission not exist with assignmentId: %s, userId: %s", param.getAssignmentId(), param.getUserId());
                    return new BusinessException(message);
                });
        LogicQuiz logicQuiz = logicQuizRepository.findById(submission.getQuizId())
                .orElseThrow(() ->
                        new BusinessException("logic quiz not exist with quizId:" + submission.getQuizId()));

        if (isTimeoutSubmission(submission, logicQuiz)) {
            throw new BusinessException("timeout submission");
        }
    }

    private boolean isTimeoutSubmission(TWLogicQuizSubmission submission, LogicQuiz logicQuiz) {
        long startTime = submission.getStartTime().getTime()/1000;
        int timeBoxInMinutes = logicQuiz.getTimeBoxInMinutes();

        long currentTime = new Date().getTime()/1000;
        return timeBoxInMinutes * 60 + startTime - currentTime < 0;
    }
}
