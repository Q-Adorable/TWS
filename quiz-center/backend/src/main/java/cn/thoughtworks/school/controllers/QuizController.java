package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.*;
import cn.thoughtworks.school.repositories.*;
import cn.thoughtworks.school.services.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v3/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private SubjectiveQuizRepository subjectiveQuizRepository;

    @Autowired
    private HomeworkQuizRepository homeworkQuizRepository;

    @Autowired
    private BasicQuizRepository basicQuizRepository;
    @Autowired
    private LogicQuizRepository logicQuizRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private UserQuizGroupRepository userQuizGroupRepository;
    @Autowired
    private BasicQuizSubmissionRepository basicQuizSubmissionRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getQuizzes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(value = "tags", defaultValue = "") String tags,
            @RequestParam(value = "makerId", defaultValue = "0") Long markerId,
            @RequestParam(value = "type", defaultValue = "") String type,
            @Auth User current) {
        List<Quiz> quizzes = new ArrayList<>();
        if ("".equals(tags) && markerId == 0L && "".equals(type)) {
            quizzes = quizRepository.findAllByOrderByIdDesc();
        } else if ((!"".equals(type)) && markerId == 0L && "".equals(tags)) {
            quizzes = quizRepository.findAllByTypeAndOrderByIdDesc(type);
        } else if ("".equals(type) && markerId != 0L && "".equals(tags)) {
            quizzes = quizRepository.findAllByMakerIdAndOrderByIdDesc(markerId);
        } else if ("".equals(type) && markerId == 0L && !"".equals(tags)) {
            quizzes = quizRepository.findAllByTagsAndOrderByIdDesc(tags.split(","));
        }

        List roles = current.getRoles();
        List<UserQuizGroup> userQuizGroups = userQuizGroupRepository.findByUserId(current.getId());
        List groupIds = userQuizGroups.stream().map(UserQuizGroup::getQuizGroupId).collect(Collectors.toList());

        List data = quizzes.stream().map(quiz -> {
            if (quiz.getType().equals("SUBJECTIVE_QUIZ")) {

                SubjectiveQuiz subjectiveQuiz = subjectiveQuizRepository
                        .findByIdAndIsAvailableIsTrue(quiz.getQuizId())
                        .orElse(new SubjectiveQuiz());

                return utils.formatAddTypeToQuiz(subjectiveQuiz, quiz.getType());
            } else if (quiz.getType().equals("LOGIC_QUIZ")) {

                LogicQuiz logicQuiz = logicQuizRepository.findByIdAndIsAvailableIsTrue(quiz.getQuizId().longValue());

                return utils.formatAddTypeToQuiz(logicQuiz, quiz.getType());
            } else if (quiz.getType().equals("HOMEWORK_QUIZ")) {
                HomeworkQuiz homeworkQuiz = homeworkQuizRepository.findByIdAndIsAvailableIsTrue(quiz.getQuizId());
                return utils.formatAddTypeToQuiz(homeworkQuiz, quiz.getType());
            } else {
                BasicQuiz basicQuiz = basicQuizRepository
                        .findByIdAndIsAvailableIsTrue(quiz.getQuizId())
                        .orElse(new BasicQuiz());

                return utils.formatAddTypeToQuiz(basicQuiz, quiz.getType());
            }
        }).collect(Collectors.toList());

        List list = (List) data.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Page result = implementPageable(list, page, size);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Page implementPageable(List list, int page, int size) {
        Pageable pageable = new PageRequest(page - 1, size);
        int start = (page - 1) * size;
        int end = start + size;

        if (end > list.size()) {
            end = list.size();
        }

        Page result = new PageImpl<>(utils.formatList(list.subList(start, end)), pageable, list.size());

        return result;
    }

    @RequestMapping(value = "/{quizIds}/assignment/{assignmentIds}", method = RequestMethod.GET)
    public ResponseEntity getAssignmentQuizzes(@PathVariable String quizIds, @PathVariable String assignmentIds) {
        List<BasicQuizSubmission> basicQuizSubmissionList = new ArrayList<>();
        String[] quizIdList = quizIds.split(",");
        String[] assignmentIdList = assignmentIds.split(",");
        for (int i = 0; i < quizIdList.length; i++) {
            List<BasicQuizSubmission> basicQuizSubmissions = basicQuizSubmissionRepository.findByQuizIdAndAssignmentIdOrderBySubmitTimeDesc(Long.parseLong(quizIdList[i]), Long.parseLong(assignmentIdList[i]));
            if (basicQuizSubmissions.size() != 0) {
                basicQuizSubmissionList.add(basicQuizSubmissions.get(0));
            }
        }
        return new ResponseEntity<>(basicQuizSubmissionList, HttpStatus.OK);
    }

}
