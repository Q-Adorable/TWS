package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.*;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.LogicQuizItemRepository;
import cn.thoughtworks.school.repositories.LogicQuizRepository;
import cn.thoughtworks.school.repositories.QuizRepository;
import cn.thoughtworks.school.repositories.UserQuizGroupRepository;
import cn.thoughtworks.school.services.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping(value = "/api/v3")
@Slf4j
public class LogicQuizController {

    private static final int EASY_LOGIC_QUIZ_ITEM_COUNT_UPPER_LIMIT = 15;

    @Autowired
    private LogicQuizRepository logicQuizRepository;
    @Autowired
    private LogicQuizItemRepository logicQuizItemRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserQuizGroupRepository userQuizGroupRepository;

    @Autowired
    private Utils utils;
    @Value("${logic-type}")
    private String type;

    @RequestMapping(value = "logicQuizzes", method = RequestMethod.POST)
    public ResponseEntity addLogicQuiz(@RequestBody LogicQuiz quiz, @Auth User user) {
        quiz.setMakerId(user.getId());
        quiz.setIsAvailable(true);
        quiz.setCreateTime(new Date());
        LogicQuiz logicQuiz = logicQuizRepository.save(quiz);
        quizRepository.save(new Quiz((long) logicQuiz.getId(), type));
        Map<String, Object> body = new HashMap<>();
        body.put("uri", "/api/v3/logicQuizzes/" + logicQuiz.getId());
        body.put("id", logicQuiz.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "logicQuizzes/{quizId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteLogicQuiz(@PathVariable Long quizId) throws BusinessException {
        LogicQuiz quiz = logicQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown logicQuizzes with id: %s", quizId)
                ));

        quiz.setIsAvailable(false);
        logicQuizRepository.save(quiz);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "logicQuizzesItem/submission", method = RequestMethod.POST)
    public ResponseEntity checkQuiz(@RequestBody Map data) throws BusinessException {
        if (Objects.isNull(data.get("id")) || Objects.isNull(data.get("answer"))) {
            throw new BusinessException("提交数据格式错误");
        }
        int id = Integer.parseInt(data.get("id").toString());
        String answer = data.get("answer").toString();
        LogicQuizItem logicQuizItem = logicQuizItemRepository.findByIdAndAnswer(id, answer);
        boolean result = !Objects.isNull(logicQuizItem);

        return new ResponseEntity(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = "logicQuizzesItem/{quizId}", method = RequestMethod.GET)
    public ResponseEntity getLogicQuizItemById(@PathVariable Integer quizId) throws BusinessException {
        LogicQuizItem logicQuizItem = logicQuizItemRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown logicQuizItem with id: %s", quizId)
                ));

        if (logicQuizItem.getCount() < EASY_LOGIC_QUIZ_ITEM_COUNT_UPPER_LIMIT) {
            logicQuizItem.setIsExample(true);
            logicQuizItem.setExampleAnswer(logicQuizItem.getAnswer());
        }

        return new ResponseEntity<>(logicQuizItem, HttpStatus.OK);
    }

    @RequestMapping(value = "/logicQuizzesItem/examples", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity getLogicQuizExamples() throws BusinessException {
        List<LogicQuizItem> examples = logicQuizItemRepository.getExampleItems(2)
                .collect(Collectors.toList());

        return new ResponseEntity<>(examples, HttpStatus.OK);
    }


    @RequestMapping(value = "logicQuizzes/{quizId}", method = RequestMethod.GET)
    public ResponseEntity getLogicQuizById(@PathVariable Long quizId) throws BusinessException {
        Map result = new HashMap();
        LogicQuiz logicQuiz = logicQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown logicQuiz with id: %s", quizId)
                ));

        Map quizzes = getLogicQuizItems(logicQuiz);

        result.put("logicQuiz", logicQuiz);
        result.put("quizzes", quizzes);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Map getLogicQuizItems(LogicQuiz logicQuiz) {
        Map result = new HashMap();
        int exampleCount = logicQuiz.getExampleCount() == 0 ? 1 : logicQuiz.getExampleCount();
        result.put("exampleItems", logicQuizItemRepository.getExampleItems(exampleCount));
        result.put("easyItems", logicQuizItemRepository.getEasyItems(logicQuiz.getEasyCount()));
        result.put("normalItems", logicQuizItemRepository.getNormalItems(logicQuiz.getNormalCount()));
        result.put("hardItems", logicQuizItemRepository.getHardItems(logicQuiz.getHardCount()));
        return result;
    }

    @RequestMapping(value = "logicQuizzes", method = RequestMethod.GET)
    public ResponseEntity getAllLogicQuizzes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(value = "makerId", defaultValue = "0") Long makerId,
            @Auth User user
    ) {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        Page logicQuizzes = searchLogicQuizzes(pageable, makerId, user);
        Page results = utils.format(logicQuizzes, pageable);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    private Page searchLogicQuizzes(Pageable pageable, Long makerId, User user) {
        Page result;
        List roles = user.getRoles();
        List<UserQuizGroup> userQuizGroups = userQuizGroupRepository.findByUserId(user.getId());
        List groupIds = userQuizGroups.stream().map(UserQuizGroup::getQuizGroupId).collect(Collectors.toList());
        if (makerId == 0L) {
            result = roles.contains(9) ? logicQuizRepository.findAllByIsAvailable(true, pageable) :
                    logicQuizRepository.findAllByIsAvailable(true, pageable);
        } else {
            result = roles.contains(9) ? logicQuizRepository.findAllByIsAvailableAndMakerId(true, makerId, pageable) :
                    logicQuizRepository.findAllByIsAvailableAndMakerId(true, makerId, pageable);
        }
        return result;
    }

    @RequestMapping(value = "logicQuizzes/ids", method = RequestMethod.GET)
    public ResponseEntity getAllLogicQuizzesById(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(value = "id", defaultValue = "0") Long id,
            @Auth User user
    ) {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        Page logicQuizzes = searchLogicQuizzesById(pageable, id, user);
        Page results = utils.format(logicQuizzes, pageable);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    private Page searchLogicQuizzesById(Pageable pageable, Long id, User user) {
        Page result;
        List roles = user.getRoles();
        List<UserQuizGroup> userQuizGroups = userQuizGroupRepository.findByUserId(user.getId());
        List groupIds = userQuizGroups.stream().map(UserQuizGroup::getQuizGroupId).collect(Collectors.toList());
        if (id == 0L) {
            result = roles.contains(9) ? logicQuizRepository.findAllByIsAvailable(true, pageable) :
                    logicQuizRepository.findAllByIsAvailable(true, pageable);
        } else {
            result = roles.contains(9) ? logicQuizRepository.findAllByIsAvailableAndId(true, id, pageable) :
                    logicQuizRepository.findAllByIsAvailableAndId(true, id, pageable);
        }
        return result;
    }

    @RequestMapping(value = "/logicQuizzes/selecting/{ids}", method = RequestMethod.GET)
    public ResponseEntity getLogicQuizzes(@PathVariable String ids) throws BusinessException {
        List<Integer> quizIds = Stream.of(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<LogicQuiz> logicQuizzes = logicQuizRepository.findAllByIsAvailableIsTrueAndIdIn(quizIds);
        return new ResponseEntity<>(utils.formatList(logicQuizzes), HttpStatus.OK);
    }

    @GetMapping(value = "/logicQuizzes/fuzzy")
    public ResponseEntity fuzzySearch(@RequestParam String type
            , @RequestParam String content
            , @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id");
        Page<LogicQuiz> logicQuizPage = null;
        if (type.equals("description")) {
            logicQuizPage = logicQuizRepository.findByDescriptionLikeAndIsAvailable(
                    "%" + content + "%", true, pageable);
        }
        return new ResponseEntity<>(utils.format(logicQuizPage, pageable), HttpStatus.OK);
    }
}


