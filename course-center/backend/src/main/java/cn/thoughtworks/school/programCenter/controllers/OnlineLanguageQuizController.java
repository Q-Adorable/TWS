package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.ReviewQuiz;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.ReviewQuizRepository;
import cn.thoughtworks.school.programCenter.services.QuizCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

//import cn.thoughtworks.school.programCenter.services.PaperCenterService;

@Controller
@RequestMapping(value = "/api")
public class OnlineLanguageQuizController {

    @Autowired
    private ReviewQuizRepository reviewQuizRepository;
    @Autowired
    private QuizCenterService quizCenterService;
    private static int ONLINE_LANGUAGE_STATUS_SUCCESS = 2;

    @RequestMapping(value = "/v2/onlineLanguageQuizzes/submission", method = RequestMethod.POST)
    public ResponseEntity submitOnlineLanguageQuiz(@RequestBody Map map, @Auth User user) {
        map.put("studentId", user.getId());

        return quizCenterService.submitOnlineLanguageQuizAnswer(map);
    }

    @RequestMapping(value = "v2/onlineLanguageQuizzes/submissions/{submissionId}/students/tasks/{taskId}/assignments/{assignmentId}/quizzes/{quizId}/runningLog", method = RequestMethod.GET)
    public ResponseEntity getOnlineLanguageRunningLog(@PathVariable Long submissionId,
                                                      @PathVariable Long assignmentId,
                                                      @PathVariable Long taskId,
                                                      @PathVariable Long quizId, @Auth User current) {
        Map result = quizCenterService.getOnlineLanguageQuizzesLogs(submissionId, assignmentId, quizId, current.getId());
        if (Objects.isNull(result)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        ReviewQuiz reviewQuiz = reviewQuizRepository.findByAssignmentIdAndQuizIdAndStudentId(assignmentId, quizId, current.getId());
        Object status = result.get("status");
//        if (Objects.nonNull(status) && Objects.equals(Integer.parseInt(status.toString()), ONLINE_LANGUAGE_STATUS_SUCCESS)) {
//            if (reviewQuiz == null) {
//                reviewQuiz = new ReviewQuiz(current.getId(), taskId, quizId, assignmentId, "已提交");
//                reviewQuizRepository.save(reviewQuiz);
//            }
//        }
        if (Objects.nonNull(status)) {
            if (reviewQuiz == null) {
                reviewQuiz = new ReviewQuiz(current.getId(), taskId, quizId, assignmentId, "已提交");
                reviewQuizRepository.save(reviewQuiz);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "v2/onlineLanguageQuizzes/{quizId}/answer", method = RequestMethod.GET)
    public ResponseEntity getOnlineLanguageQuizzessAnswer(@PathVariable Long quizId) throws Exception {
        Map quiz = quizCenterService.getOnlineLanguageQuizById(quizId);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @RequestMapping(value = "v2/onlineLanguageQuizzes/{quizId}/answerFile", method = RequestMethod.GET)
    public ResponseEntity<Resource> getAnswerFile(@PathVariable Long quizId) throws BusinessException, IOException {
        ResponseEntity answerFile = quizCenterService.getOnlineLanguageAnswerFile(quizId);
        byte[] data = answerFile.getBody().toString().getBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header("Content-Type", "application/zip")
                .body(new InputStreamResource(bis));
    }
}