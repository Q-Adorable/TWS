package cn.thoughtworks.school.services;


import cn.thoughtworks.school.entities.HomeworkQuiz;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.HomeworkQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeworkQuizService {
    @Autowired
    HomeworkQuizRepository homeworkQuizRepository;

    public HomeworkQuiz addHomeworkQuiz(HomeworkQuiz homeworkQuiz){
        return homeworkQuizRepository.save(homeworkQuiz);
    }

    public void deleteHomeworkQuiz(Long id) throws BusinessException {
        HomeworkQuiz homeworkQuiz = homeworkQuizRepository.findById(id).orElseThrow(() -> {
            String message = String.format("HomeworkQuiz is not exist with id: %s", id);
            return new BusinessException(message);
        });
        homeworkQuiz.setAvailable(false);
        homeworkQuizRepository.save(homeworkQuiz);
    }

    public void updateHomeworkQuiz(Long id , HomeworkQuiz homeworkQuiz){
        homeworkQuiz.setId(id);
        homeworkQuizRepository.save(homeworkQuiz);
    }

    public HomeworkQuiz getHomeworkQuiz(Long id) throws BusinessException {
        return homeworkQuizRepository.findById(id).orElseThrow(() -> {
            String message = String.format("HomeworkQuiz is not exist with id: %s", id);
            return new BusinessException(message);
        });
    }

}
