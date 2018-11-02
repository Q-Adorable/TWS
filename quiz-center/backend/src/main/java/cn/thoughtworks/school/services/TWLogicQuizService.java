package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.LogicQuiz;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.LogicQuizRepository;
import cn.thoughtworks.school.repositories.TWLogicQuizItemAnswerRepository;
import cn.thoughtworks.school.repositories.TWLogicQuizSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TWLogicQuizService {

    @Autowired
    TWLogicQuizSubmissionRepository submissionRepository;

    @Autowired
    LogicQuizRepository logicQuizRepository;


    @Autowired
    TWLogicQuizItemAnswerRepository answerRepository;

    public LogicQuiz createLogicQuiz(LogicQuiz logicQuiz) throws BusinessException {
        LogicQuiz savedQuiz = logicQuizRepository.save(logicQuiz);
        return savedQuiz;
    }

    public LogicQuiz getLogicQuiz(Long id) throws BusinessException {
        return logicQuizRepository
                .findById(id)
                .orElseThrow(() -> {
                    String message = String.format("logicQuiz is not exist with id: %s", id);
                    return new BusinessException(message);
                });
    }

    public void deleteLogicQuiz(Long id) throws BusinessException {
        LogicQuiz logicQuiz = logicQuizRepository
                .findById(id)
                .orElseThrow(() -> {
                    String message = String.format("logicQuiz is not exist with id: %s", id);
                    return new BusinessException(message);
                });

        logicQuiz.setIsAvailable(false);

        logicQuizRepository.save(logicQuiz);
    }

    public void updateLogicQuiz(Long id, LogicQuiz logicQuiz) throws BusinessException {
        logicQuiz.setId(id);
        logicQuizRepository.save(logicQuiz);
    }


}
