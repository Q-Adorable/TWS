package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.QuizSuggestion;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.QuizSuggestionRepository;
import cn.thoughtworks.school.programCenter.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/v2")
public class QuizSuggestionController {
    @Autowired
    private QuizSuggestionRepository quizSuggestionRepository;
    @Autowired
    private AssignmentService assignmentService;

    @PostMapping(value = "/suggestions")
    public ResponseEntity addSuggestionToQuiz(@RequestBody QuizSuggestion suggestion, @Auth User current) {
        suggestion.setCreateTime(new SimpleDateFormat("YYYY-MM-dd HH-mm-ss").format(new Date()));
        suggestion.setFromUserId(current.getId());
        suggestion = quizSuggestionRepository.save(suggestion);

        Map result = new HashMap<>();
        result.put("id", suggestion.getId());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{quizId}/suggestions")
    public ResponseEntity getSuggestionsByStudentIdAndAssignmentsAndQuizzes(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable Long quizId, @Auth User current) {
        List<QuizSuggestion> foundSuggestions = assignmentService.getQuizSuggestions(studentId, assignmentId, quizId);
        List<QuizSuggestion> result = new ArrayList<>();

        foundSuggestions.forEach(quizSuggestion -> {
            if ((Objects.isNull(quizSuggestion.getParentId()) || quizSuggestion.getParentId() == 0) &&
                    quizSuggestion.getFromUserId().equals(current.getId()) &&
                    quizSuggestion.getToUserId().equals(studentId)) {
                setChildQuizSuggestions(foundSuggestions, quizSuggestion);
                result.add(quizSuggestion);
            }
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private void setChildQuizSuggestions(List<QuizSuggestion> foundSuggestions, QuizSuggestion quizSuggestion) {
        List<QuizSuggestion> childs = foundSuggestions.stream().filter(child ->
                Objects.equals(child.getParentId(), quizSuggestion.getId())).collect(Collectors.toList());
        quizSuggestion.setChildSuggestions(childs);
    }

    @GetMapping(value = "/assignments/{assignmentId}/quizzes/{quizId}/suggestions")
    public ResponseEntity studentGetSuggestionsByStudentIdAndAssignmentsAndQuizzes(@Auth User user, @PathVariable Long assignmentId, @PathVariable Long quizId) {
        List<QuizSuggestion> result = new ArrayList<>();
        List<QuizSuggestion> foundSuggestions = assignmentService.getQuizSuggestions(user.getId(), assignmentId, quizId);

        foundSuggestions.forEach(quizSuggestion -> {
            if ((Objects.isNull(quizSuggestion.getParentId()) || quizSuggestion.getParentId() == 0)
                    && quizSuggestion.getToUserId().equals(user.getId())) {
                setChildQuizSuggestions(foundSuggestions, quizSuggestion);
                result.add(quizSuggestion);
            }
        });

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/suggestions/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateSuggestion(@PathVariable Long id, @RequestBody Map date) throws BusinessException {
        QuizSuggestion suggestion = quizSuggestionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException("该评论不存在"));

        suggestion.setContent((String) date.get("content"));
        quizSuggestionRepository.save(suggestion);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/suggestions/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSuggestion(@PathVariable Long id) throws BusinessException {
        QuizSuggestion suggestion = quizSuggestionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException("该评论不存在"));

        quizSuggestionRepository.deleteById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
