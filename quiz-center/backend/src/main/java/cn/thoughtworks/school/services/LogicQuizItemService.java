package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.LogicQuiz;
import cn.thoughtworks.school.repositories.LogicQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogicQuizItemService {

    @Autowired
    LogicQuizRepository logicQuizRepository;

    public Integer getCount() {
        LogicQuiz logicQuiz = logicQuizRepository.findByIdAndIsAvailableIsTrue(1L);
        if(logicQuiz != null) {
            return 10;
        } else {
            return 9;
        }

    }

    public long getRandom() {
        return logicQuizRepository.count();

    }
}
