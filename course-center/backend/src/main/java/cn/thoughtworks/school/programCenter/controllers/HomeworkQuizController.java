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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/api")
public class HomeworkQuizController {

  @Autowired
  private ReviewQuizRepository reviewQuizRepository;
  @Autowired
  private QuizCenterService quizCenterService;
  private static int HOMEWORK_STATUS_SUCCESS = 2;

  @RequestMapping(value = "/v2/homeworkQuizzes/submission", method = RequestMethod.POST)
  public ResponseEntity submitHomeworkQuiz(@RequestBody Map map, @Auth User user) {
    map.put("studentId", user.getId());

    return quizCenterService.submitHomeworkQuizAnswer(map);
  }

  @RequestMapping(value = "v2/homeworkQuizzes/submissions/{submissionId}/students/tasks/{taskId}/assignments/{assignmentId}/quizzes/{quizId}/runningLog", method = RequestMethod.GET)
  public ResponseEntity getHomeworkRunningLog(@PathVariable Long submissionId,
                                              @PathVariable Long assignmentId,
                                              @PathVariable Long taskId,
                                              @PathVariable Long quizId, @Auth User current) {
    Map result = quizCenterService.getHomeworkQuizzesLogs(submissionId, assignmentId, quizId, current.getId());
    if (Objects.isNull(result)) {
      return new ResponseEntity(HttpStatus.OK);
    }
    ReviewQuiz reviewQuiz = reviewQuizRepository.findByAssignmentIdAndQuizIdAndStudentId(assignmentId, quizId, current.getId());
    Object status = result.get("status");
    if (Objects.nonNull(status) && Objects.equals(Integer.parseInt(status.toString()), HOMEWORK_STATUS_SUCCESS)) {
      if (reviewQuiz == null) {
        reviewQuiz = new ReviewQuiz(current.getId(), taskId, quizId, assignmentId, "已提交", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        reviewQuizRepository.save(reviewQuiz);
      }
    }

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "v2/homeworkQuizzes/{quizId}/answer", method = RequestMethod.GET)
  public ResponseEntity getHomeworkQuizzessAnswer(@PathVariable Long quizId) throws Exception {
    Map quiz = quizCenterService.getHomeworkQuizById(quizId);
    return new ResponseEntity<>(quiz, HttpStatus.OK);
  }

  @RequestMapping(value = "v2/homeworkQuizzes/{quizId}/answerFile", method = RequestMethod.GET)
  public ResponseEntity<Resource> getAnswerFile(@PathVariable Long quizId) throws BusinessException, IOException {
    ResponseEntity answerFile = quizCenterService.getHomeworkAnswerFile(quizId);
    byte[] data = answerFile.getBody().toString().getBytes();
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    return ResponseEntity
        .ok()
        .contentLength(data.length)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .header("Content-Type", "application/zip")
        .body(new InputStreamResource(bis));
  }


  //Todo : section ? it seems need to be deleted
  @RequestMapping(value = "/student/{studentId}/section/{sectionId}/review", method = RequestMethod.GET)
  public ResponseEntity getReviewQuiz(@PathVariable Long sectionId, @PathVariable Long studentId) throws Exception {
    ReviewQuiz reviewQuiz = reviewQuizRepository.findBySectionIdAndStudentId(sectionId, studentId);
    if (reviewQuiz == null) {
      return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
    }
    return new ResponseEntity<>(reviewQuiz, HttpStatus.OK);
  }

  //Todo : section ? it seems need to be deleted
  @RequestMapping(value = "/section/{sectionId}/review", method = RequestMethod.GET)
  public ResponseEntity getStudentReviewQuiz(@PathVariable Long sectionId, @Auth User current) throws Exception {
    ReviewQuiz reviewQuiz = reviewQuizRepository.findBySectionIdAndStudentId(sectionId, current.getId());

    return new ResponseEntity<>(reviewQuiz, HttpStatus.OK);
  }
}