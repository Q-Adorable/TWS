package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.Assignment;
import cn.thoughtworks.school.programCenter.entities.AssignmentQuiz;
import cn.thoughtworks.school.programCenter.entities.ReviewQuiz;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.AssignmentQuizRepository;
import cn.thoughtworks.school.programCenter.repositories.AssignmentRepository;
import cn.thoughtworks.school.programCenter.repositories.ReviewQuizRepository;
import cn.thoughtworks.school.programCenter.services.QuizCenterService;
import cn.thoughtworks.school.programCenter.services.UserCenterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/v2")
public class QuizController {
    @Autowired
    private QuizCenterService quizCenterService;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentQuizRepository assignmentQuizRepository;
    @Autowired
    private ReviewQuizRepository reviewQuizRepository;
    private ObjectMapper oMapper = new ObjectMapper();


    @RequestMapping(value = "/assignments/{assignmentId}/quizzes/{quizId}", method = RequestMethod.GET)
    public ResponseEntity getAssignmentQuiz(@PathVariable Long assignmentId, @PathVariable Long quizId, @Auth User user) throws BusinessException {
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException("当前assignment 不存在"));

        Boolean isExist = assignment.getSelectedQuizzes().stream().map(AssignmentQuiz::getQuizId).collect(Collectors.toList()).contains(quizId);
        if (!isExist) {
            throw new BusinessException("Quiz is not in this a assignment");
        }
        List quizzes = quizCenterService.getQuizzesAndAnswerByQuizIds(quizId.toString(), assignment.getId(), user.getId(), assignment.getType());
        return new ResponseEntity<>(quizzes.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/assignments/{assignmentId}/quizzes", method = RequestMethod.GET)
    public ResponseEntity getAssignmentQuizzes(@PathVariable Long assignmentId, @Auth User current) throws BusinessException {
        List<AssignmentQuiz> assignmentQuizzes = assignmentQuizRepository.findByAssignmentId(assignmentId);
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException(String.format("Unknown assignment with id: %s", assignmentId)));

        String quizIds = assignmentQuizzes.stream()
                .map(assignmentQuiz -> assignmentQuiz.getQuizId().toString())
                .collect(Collectors.joining(","));
        List studentQuizzes = quizCenterService.getQuizzesAndAnswerByQuizIds(quizIds, assignmentId, current.getId(), assignment.getType());
        List reviewQuizzes = new ArrayList();
        if (assignment.getType().equals("BASIC_QUIZ")) {
            reviewQuizzes = reviewQuizRepository.findByAssignmentIdAndStudentId(assignmentId, current.getId());
        }
        if (reviewQuizzes.size() == 0) {
            studentQuizzes = (List) studentQuizzes.stream().map(quiz -> {
                Map map = oMapper.convertValue(quiz, Map.class);
                if (map.get("answer") != null) {
                    map.remove("answer");
                }
                if (map.get("definitionRepo") != null) {
                    map.remove("definitionRepo");
                }
                return map;
            }).collect(Collectors.toList());
        }
        return new ResponseEntity<>(studentQuizzes, HttpStatus.OK);
    }

    @RequestMapping(value = "/assignments/{assignmentId}/students/{studentId}/quizzes", method = RequestMethod.GET)
    public ResponseEntity getAssignmentQuizByStudentId(@PathVariable Long assignmentId, @PathVariable Long studentId) throws BusinessException {
        List<AssignmentQuiz> assignmentQuizzes = assignmentQuizRepository.findByAssignmentId(assignmentId);
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException(String.format("Unknown assignment with id: %s", assignmentId)));

        String quizIds = assignmentQuizzes.stream()
                .map(assignmentQuiz -> assignmentQuiz.getQuizId().toString())
                .collect(Collectors.joining(","));

        return new ResponseEntity<>(quizCenterService.getQuizzesAndAnswerByQuizIds(quizIds, assignmentId, studentId, assignment.getType()), HttpStatus.OK);
    }


    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{quizId}", method = RequestMethod.GET)
    public ResponseEntity getAssignmentQuiz(@PathVariable Long assignmentId, @PathVariable Long quizId, @PathVariable Long studentId) throws BusinessException {
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(()-> new BusinessException(String.format("Unknown assignment with id: %s", assignmentId)));

        Boolean isExist = assignment.getSelectedQuizzes().stream().map(AssignmentQuiz::getQuizId).collect(Collectors.toList()).contains(quizId);
        if (!isExist) {
            throw new BusinessException("Quiz is not in this a assignment");
        }
        List quizzes = quizCenterService.getQuizzesAndAnswerByQuizIds(quizId.toString(), assignment.getId(), studentId, assignment.getType());
        return new ResponseEntity<>(quizzes.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/assignments/{assignmentId}/quizzes/{quizId}/excellentQuizzes", method = RequestMethod.GET)
    public ResponseEntity getExcellentQuizzes(@PathVariable Long assignmentId, @PathVariable Long quizId) throws BusinessException {

        List<ReviewQuiz> reviewQuizzes = reviewQuizRepository.findAllByAssignmentIdAndQuizIdAndStatus(assignmentId, quizId, "优秀");
        List excellentReviews = reviewQuizzes.stream().map(reviewQuiz -> {
            Map userInfo = (Map) userCenterService.getUserInfo(reviewQuiz.getStudentId()).getBody();
            Map map = oMapper.convertValue(reviewQuiz, Map.class);
            map.put("userName", userInfo.get("userName"));
            return map;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(excellentReviews, HttpStatus.OK);
    }

}
