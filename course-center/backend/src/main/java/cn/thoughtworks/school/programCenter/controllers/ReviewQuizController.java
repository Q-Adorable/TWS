package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.Assignment;
import cn.thoughtworks.school.programCenter.entities.ReviewQuiz;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.AssignmentRepository;
import cn.thoughtworks.school.programCenter.repositories.ReviewQuizRepository;
import cn.thoughtworks.school.programCenter.services.QuizCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/api/v2")
public class ReviewQuizController {
    @Autowired
    private ReviewQuizRepository reviewQuizRepository;
    @Autowired
    private QuizCenterService quizCenterService;
    @Autowired
    private AssignmentRepository assignmentRepository;

    @RequestMapping(value = "/assignments/{assignmentId}/quizzes/{quizId}/review", method = RequestMethod.GET)
    public ResponseEntity getReviewQuizByAssignmentIdAndQuizId(@PathVariable Long assignmentId, @PathVariable Long quizId, @Auth User user) throws BusinessException {
        // 2018-5-17 该方法用于数据迁移，添加已提交的reviewQuiz数据，用户点击作业详情页面会先检测该作业的状态，判断是否是已提交。该方法在数据迁移结束后可删除
        dealAssignmentStatus(assignmentId, quizId, user);

        ReviewQuiz reviewQuiz = getReviewQuiz(assignmentId, quizId, user.getId());
        return new ResponseEntity<>(Objects.isNull(reviewQuiz) ? new ReviewQuiz() : reviewQuiz, HttpStatus.OK);
    }


    private void dealAssignmentStatus(Long assignmentId, Long quizId, User user) throws BusinessException {

        ReviewQuiz reviewQuiz = getReviewQuiz(assignmentId, quizId, user.getId());
        if (Objects.nonNull(reviewQuiz)) {
            return;
        }
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException("Unknow assignment"));

        List<Map> quizzes = quizCenterService.getQuizzesAndAnswerByQuizIds(quizId.toString(), assignmentId, user.getId(), assignment.getType());
        if (quizzes.size() != 0 && !Objects.equals("", quizzes.get(0).get("userAnswer").toString())) {
            reviewQuizRepository.save(new ReviewQuiz(user.getId(), assignment.getTaskId(), quizId, assignmentId, "已提交"));
        }
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{quizId}/review", method = RequestMethod.GET)
    public ResponseEntity tutorGetReviewQuiz(@PathVariable Long assignmentId, @PathVariable Long quizId, @PathVariable Long studentId) throws BusinessException {
        ReviewQuiz reviewQuiz = getReviewQuiz(assignmentId, quizId, studentId);
        if (reviewQuiz == null) {
            reviewQuiz = new ReviewQuiz();
        }
        return new ResponseEntity<>(reviewQuiz, HttpStatus.OK);
    }

    private ReviewQuiz getReviewQuiz(Long assignmentId, Long quizId, Long studentId) throws BusinessException {
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException(String.format("Unknown assignment with id: %s", assignmentId)));
        if (Objects.equals(assignment.getType(), "BASIC_QUIZ")) {
            return reviewQuizRepository.findByAssignmentIdAndStudentId(assignmentId, studentId).stream().findFirst().orElse(new ReviewQuiz());
        } else {
            return reviewQuizRepository.findByAssignmentIdAndQuizIdAndStudentId(assignmentId, quizId, studentId);
        }
    }


    @RequestMapping(value = "/review/{id}", method = RequestMethod.GET)
    public ResponseEntity getReviewQuizById(@PathVariable Long id) throws BusinessException {

        ReviewQuiz reviewQuiz = reviewQuizRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(String.format("Unknown reviewQuiz with id: %s", id)));

        return new ResponseEntity<>(reviewQuiz, HttpStatus.OK);
    }

    @RequestMapping(value = "/review/logicQuizzes", method = RequestMethod.POST)
    public ResponseEntity addLogicQuiz(@RequestBody ReviewQuiz reviewQuiz, @Auth User user) throws BusinessException {
        reviewQuiz.setStudentId(user.getId());
        reviewQuiz.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        reviewQuiz.setStatus("已完成");
        reviewQuiz.setGrade(-1);
        reviewQuizRepository.save(reviewQuiz);
        return new ResponseEntity<>(reviewQuiz, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ResponseEntity addReviewQuiz(@RequestBody ReviewQuiz reviewQuiz, @Auth User user) {
        if (reviewQuiz.getId() == null) {
            reviewQuiz.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
            reviewQuiz.setTutorId(user.getId());
            reviewQuiz = reviewQuizRepository.save(reviewQuiz);
        } else {
            ReviewQuiz oldReview = reviewQuizRepository
                    .findById(reviewQuiz.getId())
                    .orElseGet(null);

            if (oldReview == null) {
                reviewQuiz.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
                reviewQuiz = reviewQuizRepository.save(reviewQuiz);
            } else {
                oldReview.setStatus(reviewQuiz.getStatus());
                oldReview.setGrade(reviewQuiz.getGrade());
                oldReview.setTutorId(user.getId());
                reviewQuiz = reviewQuizRepository.save(oldReview);
            }
        }

        Map<String, Long> result = new HashMap<>();
        result.put("id", reviewQuiz.getId());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "review/{id}/supplement")
    public ResponseEntity submitSupplementary(@PathVariable Long id, @RequestBody ReviewQuiz reviewQuiz) throws BusinessException {
        ReviewQuiz quiz = reviewQuizRepository.findById(id).orElseThrow(() -> new BusinessException("quiz 不存在"));
        quiz.setSupplement(reviewQuiz.getSupplement());
        reviewQuizRepository.save(quiz);
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
