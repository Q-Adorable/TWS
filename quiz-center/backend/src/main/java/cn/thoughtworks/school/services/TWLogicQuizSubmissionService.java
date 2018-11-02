package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.LogicQuiz;
import cn.thoughtworks.school.entities.LogicQuizItem;
import cn.thoughtworks.school.entities.TWLogicQuizItemAnswer;
import cn.thoughtworks.school.entities.TWLogicQuizSubmission;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.LogicQuizItemRepository;
import cn.thoughtworks.school.repositories.LogicQuizRepository;
import cn.thoughtworks.school.repositories.TWLogicQuizItemAnswerRepository;
import cn.thoughtworks.school.repositories.TWLogicQuizSubmissionRepository;
import cn.thoughtworks.school.requestParams.SubmitUserAnswerParam;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TWLogicQuizSubmissionService {

    @Autowired
    TWLogicQuizSubmissionRepository submissionRepository;

    @Autowired
    LogicQuizRepository logicQuizRepository;

    @Autowired
    LogicQuizItemRepository logicQuizItemRepository;

    @Autowired
    TWLogicQuizItemAnswerRepository answerRepository;

    public TWLogicQuizSubmission findByAssignmentAndUserId(Long assignmentId, Long quizId, Long userId) throws BusinessException {
        logicQuizRepository.findById(quizId)
                .orElseThrow(()-> {
                    String message = String.format("logic quiz not exist with id: %s", quizId);
                    return new BusinessException(message);
                });

        TWLogicQuizSubmission twLogicQuizSubmission = submissionRepository.findTopByAssignmentIdAndQuizIdAndUserId(
                assignmentId,
                quizId,
                userId
        ).orElse(null);

        if(twLogicQuizSubmission == null) {
            twLogicQuizSubmission = createTWLogicQuizSubmission(assignmentId, quizId, userId);
        }

        // 添加例题
        List<TWLogicQuizItemAnswer> exampleAnswer = logicQuizItemRepository.findTop2ByCountLessThan(15)
                .map((quizItem -> {
                    return TWLogicQuizItemAnswer.builder()
                            .quizItemId(quizItem.getId())
                            .build();
                }))
                .collect(Collectors.toList());

        twLogicQuizSubmission.setTWExampleLogicQuizItemAnswer(exampleAnswer);

        return twLogicQuizSubmission;
    }

    public void submitUserAnswer(SubmitUserAnswerParam param) throws BusinessException {
        TWLogicQuizSubmission submission = submissionRepository
                .findTopByAssignmentIdAndUserId(param.getAssignmentId(), param.getUserId())
                .orElseThrow(() -> {
                    String message = String.format("TWLogicQuizSubmission not exist with assignmentId: %s, userId: %s", param.getAssignmentId(), param.getUserId());
                    return new BusinessException(message);
                });

        updateAnswer(submission.getTWLogicQuizItemAnswer(), param.getAnswers());
    }

    private void updateAnswer(List<TWLogicQuizItemAnswer> twLogicQuizItemAnswer, List<Map<String, Integer>> answers) {
        List<TWLogicQuizItemAnswer> submittedAnswer = new ArrayList<>();

        for(TWLogicQuizItemAnswer quizItem: twLogicQuizItemAnswer) {
            for(Map<String, Integer> userAnswer: answers) {
                if(quizItem.getQuizItemId().equals(userAnswer.get("quizItemId"))) {
                    quizItem.setAnswer(String.valueOf(userAnswer.get("answer")));
                    submittedAnswer.add(quizItem);
                    break;
                }
            }
        }

        List<Integer> quizItemIds = submittedAnswer
                .stream()
                .map((answer)-> answer.getQuizItemId())
                .collect(Collectors.toList());

        List<LogicQuizItem> logicQuizItems = logicQuizItemRepository
                .findByIdIn(quizItemIds)
                .collect(Collectors.toList());

        for(TWLogicQuizItemAnswer answer: submittedAnswer) {
            for(LogicQuizItem quizItem: logicQuizItems) {
                if(answer.getQuizItemId() == quizItem.getId()) {
                    answer.setIsCorrect(answer.getAnswer().equals(quizItem.getAnswer()));
                }
            }
        }

        answerRepository.saveAll(submittedAnswer);
    }

    private List<TWLogicQuizItemAnswer> initialTWLogicSubQuiz(Long quizId, Long submissionId) throws BusinessException {
        return getLoginQuizItemByQuizId(quizId).map((quizItem) -> {
            return TWLogicQuizItemAnswer.builder()
                    .quizItemId(quizItem.getId())
                    .submissionId(submissionId)
                    .build();
        }).collect(Collectors.toList());
    }

    private Stream<LogicQuizItem> getLoginQuizItemByQuizId(Long quizId) throws BusinessException {
        LogicQuiz logicQuiz = logicQuizRepository.findById(quizId)
                .orElseThrow(() -> {
                    String message = String.format("logicQuiz not exist with Id: %s", quizId);
                    return new BusinessException(message);
                });

        Stream<LogicQuizItem> easyItems = logicQuizItemRepository.getEasyItems(logicQuiz.getEasyCount());
        Stream<LogicQuizItem> normalItems = logicQuizItemRepository.getNormalItems(logicQuiz.getNormalCount());
        Stream<LogicQuizItem> hardItems = logicQuizItemRepository.getHardItems(logicQuiz.getHardCount());

        Stream<LogicQuizItem> resultingStream = StreamEx.of(easyItems)
                .append(normalItems)
                .append(hardItems);

        return resultingStream;
    }

    private TWLogicQuizSubmission createTWLogicQuizSubmission(Long assignmentId, Long quizId, Long userId) throws BusinessException {
        TWLogicQuizSubmission twLogicQuizSubmission = submissionRepository.save(
                TWLogicQuizSubmission
                        .builder()
                        .assignmentId(assignmentId)
                        .quizId(quizId)
                        .userId(userId)
                        .status("started")
                        .startTime(new Date())
                        .build()
        );

        List<TWLogicQuizItemAnswer> subQuizAnswers = initialTWLogicSubQuiz(
                twLogicQuizSubmission.getQuizId(),
                twLogicQuizSubmission.getId()
        );

        answerRepository.saveAll(subQuizAnswers);
        twLogicQuizSubmission.setTWLogicQuizItemAnswer(subQuizAnswers);

        return twLogicQuizSubmission;
    }

    public TWLogicQuizSubmission getSummary(Long assignmentId, Long quizId, Long id) {
        return submissionRepository
                .findTopByAssignmentIdAndQuizIdAndUserId(assignmentId, quizId, id)
                .orElse(TWLogicQuizSubmission.builder()
                        .status("empty")
                        .build()
                );
    }
}
