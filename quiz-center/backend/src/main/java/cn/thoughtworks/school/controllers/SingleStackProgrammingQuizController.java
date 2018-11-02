package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.HomeworkQuiz;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.enums.JobState;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.HomeworkQuizRepository;
import cn.thoughtworks.school.requestParams.CreateHomeworkParam;
import cn.thoughtworks.school.services.AmazonClientService;
import cn.thoughtworks.school.services.Utils;
import cn.thoughtworks.school.services.JenkinsService;
import cn.thoughtworks.school.services.SingleStackProgrammingQuizService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/single-stack-programming-quiz")
public class SingleStackProgrammingQuizController {

    private static int SUCCESS = 2;
    private static int FAILURE = 0;
    private static int LINEUP = 6;
    private static int LOADING = 1;
    private static int PROGRESS = 3;

    @Autowired
    SingleStackProgrammingQuizService quizService;

    @Autowired
    HomeworkQuizRepository homeworkQuizRepository;

    @Autowired
    Utils utils;

    @Autowired
    private AmazonClientService amazonClientService;

    @Value("${jenkins.callbackHost}")
    private String jenkinsCallbackHost;

    @Autowired
    JenkinsService jenkinsService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity createSingleStackProgrammingQuiz(@RequestBody CreateHomeworkParam param, @Auth User current) {
        param.setMakerId(current.getId());
        HomeworkQuiz quiz = quizService.createSingleStackProgrammingQuiz(param);

        String callbackUrl = String.format("%s/api/single-stack-programming-quiz/%s/status",
                jenkinsCallbackHost,
                quiz.getId()
        );

        jenkinsService.triggerSingleStackProgrammingQuizJob(param.getDefinitionRepo(), callbackUrl);
        Map<String, Object> result = new HashMap<>();
        result.put("uri", "/api/v3/homeworkQuizzes/" + quiz.getId());
        result.put("id", quiz.getId());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.POST)
    public ResponseEntity updateSingleStackProgrammingQuiz(@PathVariable Long id,
                                                           @RequestParam(value = "status") int statusInt,
                                                           @RequestParam(value = "result", required = false) String result,
                                                           @RequestParam(value = "script", required = false) MultipartFile script,
                                                           @RequestParam(value = "answer", required = false) MultipartFile answer,
                                                           @RequestParam(value = "readme", required = false) MultipartFile readme,
                                                           @RequestParam(value = "answerDescription", required = false) MultipartFile answerDescription,
                                                           @RequestParam(value = "buildNumber", required = false) String buildNumber,
                                                           @RequestParam(value = "templateRepository", required = false) String templateRepository) throws BusinessException {


        HomeworkQuiz quiz = quizService.findById(id);
        quiz.setStatus(statusInt);

        quiz.setJobMessage(result);

        if (buildNumber != null) {
            quiz.setBuildNumber(Long.parseLong(buildNumber));
        }
        if(statusInt == JobState.FAILURE){
            quiz.setAvailable(false);
            homeworkQuizRepository.save(quiz);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (statusInt != JobState.SUCCESS) {
            homeworkQuizRepository.save(quiz);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        String answerPath = amazonClientService.uploadFile(answer, "homework-answer");
        String evaluateScript = amazonClientService.uploadFile(script, "homework-script");

        quiz.setTemplateRepository(templateRepository);
        quiz.setDescription(utils.readFileToString(readme));
        quiz.setAnswerDescription(utils.readFileToString(answerDescription));
        quiz.setAnswerPath(answerPath);
        quiz.setEvaluateScript(evaluateScript);

        homeworkQuizRepository.save(quiz);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "{quizId}/answerFile")
    public void downloadAnswerFile(@PathVariable Long quizId, HttpServletResponse response) throws BusinessException, IOException {
        HomeworkQuiz homeworkQuiz = homeworkQuizRepository
                .findById(quizId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown homeworkQuiz with id: %s", quizId)
                ));
        InputStream inputStream = amazonClientService.getInputStream(homeworkQuiz.getAnswerPath(), "homework-answer");
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }
}
