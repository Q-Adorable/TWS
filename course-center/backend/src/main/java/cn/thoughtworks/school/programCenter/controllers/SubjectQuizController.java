package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.ReviewQuiz;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.ReviewQuizRepository;
import cn.thoughtworks.school.programCenter.services.QuizCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v2/")
public class SubjectQuizController {
    @Autowired
    private QuizCenterService quizCenterService;
    @Autowired
    private ReviewQuizRepository reviewQuizRepository;

    @RequestMapping(value = "subjectQuizzes/tasks/{taskId}/assignments/{assignmentId}/quizzes/{quizId}/submission", method = RequestMethod.POST)
    public ResponseEntity submitAnswer(@RequestBody Map data, @PathVariable Long taskId, @PathVariable Long assignmentId, @PathVariable Long quizId, @Auth User current) throws BusinessException {
        ResponseEntity resp = quizCenterService.submitSubjectiveAnswer(assignmentId, quizId, current.getId(), data);
        if (resp.getStatusCode() != HttpStatus.CREATED) {
            throw new BusinessException("提交失败");
        }
        ReviewQuiz reviewQuiz = reviewQuizRepository.findByAssignmentIdAndQuizIdAndStudentId(assignmentId, quizId, current.getId());
        if (reviewQuiz == null) {
            ReviewQuiz newReviewQuiz = new ReviewQuiz(current.getId(), taskId, quizId, assignmentId, "已提交",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
            reviewQuiz = reviewQuizRepository.save(newReviewQuiz);
        }
        HashMap map = new HashMap();
        map.put("id", reviewQuiz.getId());
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
