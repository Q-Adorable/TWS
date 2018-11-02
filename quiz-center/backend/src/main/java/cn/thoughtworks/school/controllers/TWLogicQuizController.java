package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.LogicQuiz;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.services.TWLogicQuizService;
import cn.thoughtworks.school.services.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


// TODO 用来替换 LogicQuizController类
@RestController
@RequestMapping(value = "/api/tw-logic-quizzes")
public class TWLogicQuizController {

    @Autowired
    private TWLogicQuizService logicQuizService;
    @Autowired
    private Utils utils;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity addLogicQuiz(@RequestBody LogicQuiz quiz, @Auth User user) throws BusinessException {
        quiz.setMakerId(user.getId());
        quiz.setCreateTime(new Date());
        logicQuizService.createLogicQuiz(quiz);
        return new ResponseEntity<>(quiz, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{quizId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteLogicQuiz(@PathVariable Long quizId) throws BusinessException {
        logicQuizService.deleteLogicQuiz(quizId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{quizId}", method = RequestMethod.GET)
    public ResponseEntity getLogicQuizItemById(@PathVariable Long quizId) throws BusinessException {
        LogicQuiz logicQuiz = logicQuizService.getLogicQuiz(quizId);
        return new ResponseEntity<>(logicQuiz, HttpStatus.OK);
    }

    @RequestMapping(value = "/{quizId}", method = RequestMethod.PUT)
    public ResponseEntity getLogicQuizById(@PathVariable Long quizId,
                                           @RequestBody LogicQuiz logicQuiz) throws BusinessException {
        logicQuizService.updateLogicQuiz(quizId, logicQuiz);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
