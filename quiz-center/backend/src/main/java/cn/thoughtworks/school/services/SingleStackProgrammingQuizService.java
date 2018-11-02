package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.HomeworkQuiz;
import cn.thoughtworks.school.entities.Quiz;
import cn.thoughtworks.school.enums.JobState;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.HomeworkQuizRepository;
import cn.thoughtworks.school.repositories.QuizRepository;
import cn.thoughtworks.school.requestParams.CreateHomeworkParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SingleStackProgrammingQuizService {

    private static int SUCCESS = 2;
    private static int FAILURE = 0;
    private static int LINEUP = 6;
    private static int LOADING = 1;
    private static int PROGRESS = 3;

    @Autowired
    HomeworkQuizRepository homeworkQuizRepository;
    @Autowired
    QuizRepository quizRepository;


    public HomeworkQuiz createSingleStackProgrammingQuiz(CreateHomeworkParam param) {

        HomeworkQuiz quiz = HomeworkQuiz.builder()
                .templateRepository("生成中")
                .definitionRepo(param.getDefinitionRepo())
                .makerId(param.getMakerId())
                .createTime(new Date())
                .homeworkName(param.getTitle()) //todo 这里命名要统一
                .stackId(param.getStackId())
                .remark(param.getRemark())
                .quizGroupId(param.getQuizGroupId())
                .jobMessage("生成中")
                .status(JobState.LINEUP)
                .isAvailable(true)
                .build();

        quiz = homeworkQuizRepository.save(quiz);
        quizRepository.save(new Quiz(quiz.getId(), "HOMEWORK_QUIZ"));

        return quiz;
    }

    public HomeworkQuiz findById(Long id) throws BusinessException {
        return homeworkQuizRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("Can't find homework quiz with id: %s", id);
                    return new BusinessException(message);
                });
    }
}
