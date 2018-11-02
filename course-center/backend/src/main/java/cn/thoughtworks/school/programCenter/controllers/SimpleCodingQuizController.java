package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.services.QuizCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/simpleCodingQuizzes")
public class SimpleCodingQuizController {

    @Autowired
    private QuizCenterService quizCenterService;

    @PostMapping("/submission")
    public ResponseEntity submitHomeworkQuiz(@RequestBody Map map, @Auth User user) {
        map.put("studentId", user.getId());

        return quizCenterService.submitHomeworkQuizAnswer(map);
    }

    @GetMapping("/users/{userId}/assignments/{assignmentId}/quizzes/{quizId}/submissions")
    public ResponseEntity getHomeworkRunningLog(@PathVariable Long assignmentId,
                                                @PathVariable Long userId,
                                                @PathVariable Long quizId) {
        Map result = quizCenterService.getSubmissionByAssignmentIdAndQuizId(userId, assignmentId, quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "{quizId}/answerFile", method = RequestMethod.GET)
    public ResponseEntity<Resource> getAnswerFile(@PathVariable Long quizId) {
        ResponseEntity answerFile = quizCenterService.getSimpleCodingAnswerFile(quizId);
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