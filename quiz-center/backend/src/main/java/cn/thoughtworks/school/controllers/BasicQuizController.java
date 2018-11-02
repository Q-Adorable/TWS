package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.*;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.*;
import cn.thoughtworks.school.services.Utils;
import cn.thoughtworks.school.services.UserCenterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api/v3/basicQuizzes")
@Slf4j
public class BasicQuizController {

    @Autowired
    private BasicQuizRepository basicQuizRepository;

    @Autowired
    private BasicQuizChoicesRepository basicQuizChoicesRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizGroupRepository quizGroupRepository;
    @Autowired
    private BasicQuizSubmissionRepository basicQuizSubmissionRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private UserQuizGroupRepository userQuizGroupRepository;
    @Autowired
    UserCenterService userCenterService;

    private Boolean IS_AVAILABLE_TRUE = true;
    @Value("${basic-type}")
    private String type;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity saveBasicQuiz(@RequestBody Map data, @Auth User user) {
        String answer = formatAnswer(data);
        String type = (String) data.get("type");
        Long quizGroupId = Long.parseLong(data.get("quizGroupId").toString());
        BasicQuiz basicQuiz = new BasicQuiz((String) data.get("description"), type, answer);
        basicQuiz.setMakerId(user.getId());
        basicQuiz.setAvailable(true);
        basicQuiz.setQuizGroupId(quizGroupId);
        basicQuiz.setCreateTime(new SimpleDateFormat("YYYY-MM-dd HH-mm-ss").format(new Date()));
        basicQuiz.setTags(utils.formatTags((List<String>) data.get("tags"), user.getId()));
        basicQuiz = basicQuizRepository.save(basicQuiz);

        if (!type.equals("BASIC_BLANK_QUIZ")) {


            List<BasicQuizChoices> choices = genBasicQuizChoices((List<String>) data.get("choices"), basicQuiz.getId());
            basicQuizChoicesRepository.saveAll(choices);
        }

        quizRepository.save(new Quiz(basicQuiz.getId(), type));
        Map<String, Object> body = new HashMap<>();
        body.put("uri", "/api/v3/basicQuizzes/" + basicQuiz.getId());
        body.put("id", basicQuiz.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    private List<BasicQuizChoices> genBasicQuizChoices(List<String> choices, Long quizId) {
        List<BasicQuizChoices> result = new ArrayList<>();

        int i = 0;
        for (String choice : choices) {
            if (Objects.equals("", choice.trim())) {
                continue;
            }
            BasicQuizChoicesComplexPK complexPk = new BasicQuizChoicesComplexPK(i, quizId);
            BasicQuizChoices basicQuizChoice = new BasicQuizChoices(complexPk, choice);
            result.add(basicQuizChoice);
            i++;
        }
        return result;
    }

    private String formatAnswer(Map data) {
        if (data.get("type").equals("MULTIPLE_CHOICE")) {
            List<String> answers = ((List<String>) data.get("answer")).stream()
                    .collect(Collectors.toList());
            return answers.stream().reduce("", (p, n) -> p + (p.equals("") ? "" : ",") + n);
        }

        return (String) data.get("answer");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getBasicQuiz(@PathVariable Long id) throws BusinessException {
        BasicQuiz basicQuiz = basicQuizRepository
                .findByIdAndIsAvailableIsTrue(id)
                .orElseThrow(() -> new BusinessException("该题不存在"));

        List<Map> users = userCenterService.getUsersByIds(basicQuiz.getId().toString());

        Map basicMap = formatBasicQuizByUsers(basicQuiz, users);

        return new ResponseEntity<>(basicMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}", method = RequestMethod.GET)
    public ResponseEntity getBasicQuizList(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable String ids) throws BusinessException {

        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<BasicQuiz> basicQuizzes = basicQuizRepository.findAllByIsAvailableIsTrueAndIdIn(idList);


        List<BasicQuizSubmission> basicQuizSubmissions = basicQuizSubmissionRepository.findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(idList, assignmentId, studentId);

        List<Map> results = basicQuizzes.stream()
                .map(basicQuiz -> utils.addUserAnswerWithBasicQuiz(basicQuiz, basicQuizSubmissions))
                .collect(Collectors.toList());

        return new ResponseEntity<>(results, HttpStatus.OK);

    }


    private List formatChoices(List<BasicQuizChoices> choices) {
        List result = new ArrayList();


        for (BasicQuizChoices choice : choices) {
            result.add(choice.getChoice());
        }

        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity updateBasicQuiz(@PathVariable Long id, @RequestBody Map updateQuiz, @Auth User user) throws BusinessException {
        BasicQuiz basicQuiz = basicQuizRepository
                .findByIdAndIsAvailableIsTrue(id)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown basicQuiz with id: %s", id)
                ));

        String type = basicQuiz.getType();
        updateQuiz.put("type", type);
        String answer = formatAnswer(updateQuiz);

        basicQuiz.setAnswer(answer);
        basicQuiz.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        basicQuiz.setDescription((String) updateQuiz.get("description"));
        basicQuiz.setMakerId(user.getId());
        basicQuiz.setTags(utils.formatTags((List<String>) updateQuiz.get("tags"), user.getId()));
        basicQuiz.setQuizGroupId(Long.parseLong(updateQuiz.get("quizGroupId").toString()));

        if (!type.equals("BASIC_BLANK_QUIZ")) {
            List<String> options = (List<String>) updateQuiz.get("choices");
            List<BasicQuizChoices> choices = basicQuiz.getChoices();
            int i = 0;
            for (String option : options) {
                choices.get(i).setChoice(option);
                i++;
            }

            basicQuiz.setChoices(choices);
        }
        basicQuizRepository.save(basicQuiz);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getBasicQuizzes(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                          @RequestParam(value = "type", defaultValue = "") String type,
                                          @RequestParam(value = "tags", defaultValue = "") String tags,

                                          @RequestParam(value = "makerId", defaultValue = "0") Long makerId,
                                          @Auth User user) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<BasicQuiz> results;
        List roles = user.getRoles();

        List<UserQuizGroup> userQuizGroups = userQuizGroupRepository.findByUserId(user.getId());
        List groupIds = userQuizGroups.stream().map(UserQuizGroup::getQuizGroupId).collect(Collectors.toList());
        if ("".equals(type)) {
            results = getBasicQuizzesBySearchType(tags, makerId, pageable, user, roles, groupIds);
        } else {
            results = getBasicQuizzesByTypeAndSearchType(type, tags, makerId, pageable, roles, groupIds);

        }

        List<BasicQuiz> basicQuizzes = results.getContent().stream().collect(Collectors.toList());

        String userIdStr = utils.getMakerIds(basicQuizzes);

        List<Map> users = userCenterService.getUsersByIds(userIdStr);

        List<Map> quizzes = basicQuizzes.stream()
                .sorted(Comparator.comparing(BasicQuiz::getId).reversed())
                .map(basicQuiz -> formatBasicQuizByUsers(basicQuiz, users))
                .collect(Collectors.toList());

        Page resultPage = new PageImpl<>(quizzes, pageable, results.getTotalElements());

        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    public ResponseEntity getBasicQuizzesById(@RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                              @RequestParam(value = "id", defaultValue = "0") Long id,
                                              @Auth User user) {
        Pageable pageable = new PageRequest(page - 1, size, Sort.Direction.DESC, "id");
        Page<BasicQuiz> results;
        if (id == 0) {
            results = basicQuizRepository.findAll(pageable);
        } else {
            results = basicQuizRepository.findAllById(id, pageable);
        }
        List<BasicQuiz> basicQuizzes = results.getContent().stream().collect(Collectors.toList());
        String userIdStr = utils.getMakerIds(basicQuizzes);

        List<Map> users = userCenterService.getUsersByIds(userIdStr);
        List<Map> quizzes = basicQuizzes.stream()
                .sorted(Comparator.comparing(BasicQuiz::getId).reversed())
                .map(basicQuiz -> formatBasicQuizByUsers(basicQuiz, users))
                .collect(Collectors.toList());
        Page resultPage = new PageImpl<>(quizzes, pageable, results.getTotalElements());

        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }


    private Page<BasicQuiz> getBasicQuizzesBySearchType(String tags, Long makerId, Pageable pageable, User user, List roles, List groupIds) {
        Page<BasicQuiz> results;

        if (tags.equals("") && makerId == 0L) {
            results = basicQuizRepository.findAllByIsAvailable(IS_AVAILABLE_TRUE, pageable);
        } else if ((!tags.equals("")) && makerId == 0L) {
            String[] tagNames = tags.split(",");
            results = basicQuizRepository.findAllByIsAvailableAndTag(tagNames, pageable);
        } else if (tags.equals("") && makerId != 0L) {
            results = basicQuizRepository.findAllByIsAvailableAndMakerId(IS_AVAILABLE_TRUE, makerId, pageable);
        } else {
            String[] tagNames = tags.split(",");
            results = basicQuizRepository.findAllByIsAvailableAndTagsAndMakerId(tagNames, makerId, pageable);
        }
        return results;
    }

    private Page<BasicQuiz> getBasicQuizzesByTypeAndSearchType(String type, String tags, Long makerId, Pageable pageable, List roles, List groupIds) {
        Page<BasicQuiz> results;
        if (tags.equals("") && makerId == 0L) {
            results = basicQuizRepository.findAllByTypeAndIsAvailable(type, IS_AVAILABLE_TRUE, pageable);
        } else if ((!tags.equals("")) && makerId == 0L) {
            String[] tagNames = tags.split(",");
            results = basicQuizRepository.findAllByTypeAndIsAvailableAndTag(type, tagNames, pageable);
        } else if (tags.equals("") && makerId != 0L) {
            results = basicQuizRepository.findAllByTypeAndIsAvailableAndMakerId(type, IS_AVAILABLE_TRUE, makerId, pageable);
        } else {
            String[] tagNames = tags.split(",");
            results = basicQuizRepository.findAllByTypeAndIsAvailableAndTagsAndMakerId(type, tagNames, makerId, pageable);
        }

        return results;
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes", method = RequestMethod.POST)
    public ResponseEntity submitBasicQuiz(@PathVariable Long studentId, @PathVariable Long assignmentId, @RequestBody List<Map> data) throws BusinessException {
        List<Boolean> quizSubmissionList = data.stream().map(item -> {
            Long quizId = Long.valueOf((int) item.get("quizId"));
            BasicQuiz basicQuiz = basicQuizRepository
                    .findByIdAndIsAvailableIsTrue(quizId)
                    .orElse(new BasicQuiz());

            String userAnswer = (String) item.get("userAnswer");
            BasicQuizSubmission basicQuizSubmission = new BasicQuizSubmission(assignmentId, (long) quizId, studentId, userAnswer);
            if (basicQuiz.getAnswer().equals(userAnswer)) {
                basicQuizSubmission.setCorrect(true);
            } else {
                basicQuizSubmission.setCorrect(false);
            }
            basicQuizSubmission.setSubmitTime(new SimpleDateFormat("YYYY-MM-dd HH-mm-ss").format(new Date()));
            basicQuizSubmissionRepository.save(basicQuizSubmission);
            return basicQuizSubmission.getCorrect();
        }).collect(Collectors.toList());
        return new ResponseEntity<>(quizSubmissionList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBasicQuiz(@PathVariable Long id) throws BusinessException {

        BasicQuiz basicQuiz = basicQuizRepository
                .findByIdAndIsAvailableIsTrue(id)
                .orElse(null);

        if (basicQuiz != null) {
            basicQuiz.setAvailable(false);
            basicQuizRepository.save(basicQuiz);
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/selecting/{ids}", method = RequestMethod.GET)
    public ResponseEntity getBasicQuizzes(@PathVariable String ids) throws BusinessException {
        List<Long> quizIds = Stream.of(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<BasicQuiz> basicQuizzes = basicQuizRepository.findAllByIsAvailableIsTrueAndIdIn(quizIds);

        String userIdStr = utils.getMakerIds(basicQuizzes);

        List<Map> users = userCenterService.getUsersByIds(userIdStr);

        List quizzes = basicQuizzes.stream()
                .map(basicQuiz -> formatBasicQuizByUsers(basicQuiz, users))
                .collect(Collectors.toList());

        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    private Map formatBasicQuizByUsers(BasicQuiz basicQuiz, List<Map> users) {
        ObjectMapper oMapper = new ObjectMapper();

        Map data = oMapper.convertValue(basicQuiz, Map.class);
        String userName = "";
        if (data.get("makerId") != null) {
            userName = utils.getQuizMaker(users, data);
        }
        data.put("maker", userName);
        List<BasicQuizChoices> choices = basicQuiz.getChoices();
        data.put("choices", formatChoices(choices));
        if (basicQuiz.getQuizGroupId() != null) {
            QuizGroup quizGroup = quizGroupRepository
                    .findById(basicQuiz.getQuizGroupId())
                    .orElse(new QuizGroup());

            data.put("quizGroupName", quizGroup.getName());
        }
        if ("BASIC_BLANK_QUIZ".equals(basicQuiz.getType())) {
            data.remove("choices");
        } else if ("MULTIPLE_CHOICE".equals(basicQuiz.getType())) {
            String[] answers = basicQuiz.getAnswer().split(",");

            data.put("answer", answers);
        }

        return data;
    }


    @GetMapping(value = "/fuzzy")
    public ResponseEntity fuzzySearch(@RequestParam String type
            , @RequestParam String content
            , @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id");
        Page<BasicQuiz> basicQuizs = null;
        if (type.equals("description")) {
            basicQuizs = basicQuizRepository.findByDescriptionLikeAndIsAvailable("%" + content + "%", true, pageable);
        }

        return new ResponseEntity<>(utils.format(basicQuizs, pageable), HttpStatus.OK);
    }
}


