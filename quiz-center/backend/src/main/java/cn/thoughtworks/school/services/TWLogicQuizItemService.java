package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.LogicQuiz;
import cn.thoughtworks.school.entities.LogicQuizItem;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.LogicQuizItemRepository;
import cn.thoughtworks.school.repositories.LogicQuizRepository;
import cn.thoughtworks.school.repositories.TWLogicQuizItemAnswerRepository;
import cn.thoughtworks.school.repositories.TWLogicQuizSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TWLogicQuizItemService {

    public static Integer EASY_LOGIC_QUIZ_ITEM_COUNT_UPPER_LIMIT = 15;

    @Autowired
    LogicQuizItemRepository logicQuizItemRepository;

    public List<LogicQuizItem> getExamples() throws BusinessException {
        return logicQuizItemRepository.findTop2ByCountLessThan(EASY_LOGIC_QUIZ_ITEM_COUNT_UPPER_LIMIT)
                .map(quizItem -> {
                    quizItem.setIsExample(true);
                    quizItem.setExampleAnswer(quizItem.getAnswer());
                    return quizItem;
                })
                .collect(Collectors.toList());
    }

}
