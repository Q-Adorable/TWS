package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.*;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.*;
import cn.thoughtworks.school.services.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api/v3/subjectiveQuizzes")
@Slf4j
public class SubjectiveQuizController {
    @Autowired
    private SubjectiveQuizRepository subjectiveQuizRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired

    private QuizGroupRepository quizGroupRepository;

    @Autowired
    private SubjectiveQuizSubmissionRepository subjectiveQuizSubmissionRepository;
    @Autowired
    private UserQuizGroupRepository userQuizGroupRepository;
    @Value("${subjective-type}")
    private String type;


    @RequestMapping(value = "", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity addSubjectiveQuiz(@RequestBody Map data, @Auth User current) {
        Long quizGroupId = Long.parseLong(data.get("quizGroupId").toString());
        SubjectiveQuiz quiz = new SubjectiveQuiz();
        quiz.setMakerId(current.getId());
        quiz.setReferenceNumber(0L);
        quiz.setIsAvailable(true);
        quiz.setQuizGroupId(quizGroupId);
        quiz.setDescription((String) data.get("description"));
        quiz.setTags(utils.formatTags((List<String>) data.get("tags"), current.getId()));
        quiz.setRemark((String) data.get("remark"));
        quiz.setCreateTime(new SimpleDateFormat("YYYY-MM-dd HH-mm-ss").format(new Date()));
        SubjectiveQuiz newSubjectiveQuiz = subjectiveQuizRepository.save(quiz);
        quizRepository.save(new Quiz(newSubjectiveQuiz.getId(), type));
        Map<String, Object> body = new HashMap<>();
        body.put("uri", "/api/v3/subjectiveQuizzes/" + newSubjectiveQuiz.getId());
        body.put("id", newSubjectiveQuiz.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getSubjectiveQuizzes(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(value = "tags", defaultValue = "") String tags,
            @RequestParam(value = "makerId", defaultValue = "0") Long makerId) throws BusinessException {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, size, sort);

        Page subjectiveQuizzesPage;
        if (tags.equals("") && makerId == 0L) {
            subjectiveQuizzesPage = subjectiveQuizRepository.findAllByIsAvailable(true, pageable);
        } else if ((!tags.equals("")) && makerId == 0L) {
            String[] tagNames = tags.split(",");
            subjectiveQuizzesPage = subjectiveQuizRepository.findAllByIsAvailableAndTags(tagNames, pageable);

        } else if (tags.equals("") && makerId != 0L) {
            subjectiveQuizzesPage = subjectiveQuizRepository.findAllByIsAvailableAndMakerId(true, makerId, pageable);
        } else {
            subjectiveQuizzesPage = subjectiveQuizRepository.findAllByIsAvailableAndTagsAndMakerId(true, tags, makerId, pageable);
        }
        Page result = utils.format(subjectiveQuizzesPage, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    public ResponseEntity getSubjectiveQuizzesById(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(value = "id", defaultValue = "0") Long id) throws BusinessException {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        Page subjectiveQuizzesPage;
        if (id == 0) {
            subjectiveQuizzesPage = subjectiveQuizRepository.findAll(pageable);
        } else {
            subjectiveQuizzesPage = subjectiveQuizRepository.findAllById(id, pageable);
        }

        Page result = utils.format(subjectiveQuizzesPage, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{quizId}", method = RequestMethod.GET)
    public ResponseEntity getSubjectiveQuiz(@PathVariable Long quizId) throws BusinessException {
        log.info(String.format("GET:  /api/v3/subjectiveQuizzes/%s", quizId));

        SubjectiveQuiz subjectiveQuiz = subjectiveQuizRepository
                .findByIdAndIsAvailableIsTrue(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown subjectiveQuiz with id: %s", quizId)
                ));

        ObjectMapper oMapper = new ObjectMapper();
        Map data = oMapper.convertValue(subjectiveQuiz, Map.class);

        if (subjectiveQuiz.getQuizGroupId() != null) {
            QuizGroup quizGroup = quizGroupRepository
                    .findById(subjectiveQuiz.getQuizGroupId())
                    .orElse(new QuizGroup());
            data.put("quizGroupName", quizGroup.getName());
        }

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/{quizId}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity editSubjectiveQuiz(@PathVariable Long quizId, @RequestBody Map data, @Auth User current) throws BusinessException {

        SubjectiveQuiz subjectiveQuiz = subjectiveQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown subjectiveQuiz with id: %s", quizId)
                ));

        subjectiveQuiz.setDescription((String) data.get("description"));
        subjectiveQuiz.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        subjectiveQuiz.setTags(utils.formatTags((List<String>) data.get("tags"), current.getId()));
        subjectiveQuiz.setRemark((String) data.get("remark"));
        subjectiveQuiz.setQuizGroupId(Long.parseLong(data.get("quizGroupId").toString()));

        subjectiveQuizRepository.save(subjectiveQuiz);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{quizId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSubjectiveQuiz(@PathVariable Long quizId) throws BusinessException {

        SubjectiveQuiz subjectiveQuiz = subjectiveQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown subjectiveQuiz with id: %s", quizId)
                ));

        subjectiveQuiz.setIsAvailable(false);

        subjectiveQuizRepository.save(subjectiveQuiz);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/selecting/{ids}", method = RequestMethod.GET)
    public ResponseEntity getSubjectiveQuizzes(@PathVariable String ids) throws BusinessException {
        List<Long> quizIds = Stream.of(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<SubjectiveQuiz> subjectiveQuizzes = subjectiveQuizRepository.findAllByIsAvailableIsTrueAndIdIn(quizIds);
        return new ResponseEntity<>(utils.formatList(subjectiveQuizzes), HttpStatus.OK);
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}", method = RequestMethod.GET)
    public ResponseEntity getSubjectiveQuizzesInfo(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable String ids) throws BusinessException {
        List<Long> quizIds = Stream.of(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<SubjectiveQuiz> subjectiveQuizzes = subjectiveQuizRepository.findAllByIsAvailableIsTrueAndIdIn(quizIds);
        List<SubjectiveQuizSubmission> quizSubmissions = subjectiveQuizSubmissionRepository.findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(quizIds, assignmentId, studentId);
        List subjectiveQuizzesInfo = subjectiveQuizzes.stream()
                .map((subjectiveQuiz -> utils.addUserAnswer(subjectiveQuiz, quizSubmissions)))
                .collect(Collectors.toList());
        return new ResponseEntity<>(subjectiveQuizzesInfo, HttpStatus.OK);
    }

    @RequestMapping(value = "/students/{studentId}/assignments/{assignmentId}/quizzes/{quizId}", method = RequestMethod.POST)
    public ResponseEntity submitAnswer(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable Long quizId, @RequestBody Map map) throws BusinessException {

        SubjectiveQuizSubmission quizSubmission = new SubjectiveQuizSubmission(assignmentId, quizId, studentId, (String) map.get("userAnswer"));
        quizSubmission.setSubmitTime(new SimpleDateFormat("YYYY-MM-dd HH-mm-ss").format(new Date()));
        subjectiveQuizSubmissionRepository.save(quizSubmission);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/oldAssignments/{oldAssignmentId}/quizzes/{quizId}/newAssignments/{newAssignmentId}", method = RequestMethod.GET)
    public ResponseEntity subjectiveQuizDataMigration(@PathVariable Long oldAssignmentId, @PathVariable Long quizId, @PathVariable Long newAssignmentId) throws BusinessException {
        List<SubjectiveQuizSubmission> subjectiveQuizSubmissions = subjectiveQuizSubmissionRepository.findAllByAssignmentIdAndQuizId(oldAssignmentId, quizId);
        if (subjectiveQuizSubmissions.size() != 0) {
            for (SubjectiveQuizSubmission subjectiveQuizSubmission : subjectiveQuizSubmissions) {
                subjectiveQuizSubmission.setAssignmentId(newAssignmentId);
                subjectiveQuizSubmissionRepository.save(subjectiveQuizSubmission);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/fuzzy")
    public ResponseEntity fuzzySearch(@RequestParam String type
            , @RequestParam String content
            , @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id");
        Page<SubjectiveQuiz> subjectiveQuizs = null;
        if (type.equals("remark")) {
            subjectiveQuizs = subjectiveQuizRepository.findByRemarkLikeAndIsAvailable(
                    "%" + content + "%", true, pageable);
        } else if (type.equals("description")) {
            subjectiveQuizs = subjectiveQuizRepository.findByDescriptionLikeAndIsAvailable(
                    "%" + content + "%", true, pageable);
        }
        return new ResponseEntity<>(utils.format(subjectiveQuizs, pageable), HttpStatus.OK);
    }
}
