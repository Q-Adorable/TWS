package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.*;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.*;
import cn.thoughtworks.school.programCenter.services.UserCenterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api")
public class ExcellentQuizCommentController {
  @Autowired
  private UserCenterService userCenterService;
  @Autowired
  private ExcellentQuizCommentRepository excellentQuizCommentRepository;
  @Autowired
  private SectionVideoAddressRepository sectionVideoAddressRepository;

  private ObjectMapper oMapper = new ObjectMapper();

  @RequestMapping(value = "/v2/excellentQuizComments", method = RequestMethod.POST)
  public ResponseEntity submitExcellentQuizComment(@RequestBody ExcellentQuizComment excellentQuizComment, @Auth User current) throws BusinessException {
    excellentQuizComment.setCommenterId(current.getId());
    excellentQuizComment.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
    excellentQuizCommentRepository.save(excellentQuizComment);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(value = "/v2/excellentQuizComments/students/{studentId}/assignments/{assignmentId}/quizzes/{quizId}", method = RequestMethod.GET)
  public ResponseEntity getExcellentQuizComment(@PathVariable Long studentId, @PathVariable Long assignmentId, @PathVariable Long quizId) throws BusinessException {
    List<ExcellentQuizComment> excellentQuizComments = excellentQuizCommentRepository.findByAssignmentIdAndQuizIdAndStudentIdOrderByCreateTimeDesc(assignmentId, quizId, studentId);
    String ids = excellentQuizComments.stream().map(excellentQuizComment -> excellentQuizComment.getCommenterId().toString()).collect(Collectors.joining(","));
    List users = userCenterService.getUsersByIds(ids);

    List comments = excellentQuizComments.stream().map(excellentQuizComment -> {
      Map map = oMapper.convertValue(excellentQuizComment, Map.class);
      Optional currentUser = users.stream()
          .filter(user -> {
            Map userMap = oMapper.convertValue(user, Map.class);
            return userMap.get("id").toString().equals(map.get("commenterId").toString());
          }).findFirst();
      if (currentUser.isPresent()) {
        Map user = oMapper.convertValue(currentUser.get(), Map.class);
        map.put("userName", user.get("username"));
      }
      return map;
    }).collect(Collectors.toList());
    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @RequestMapping(value = "tasks/{taskId}/sections/{sectionId}/excellentAssignmentVideo", method = RequestMethod.GET)
  public ResponseEntity getExcellentHomeworkVideo(@PathVariable Long sectionId, @PathVariable Long taskId) throws BusinessException {
    SectionVideoAddress videoAddress = sectionVideoAddressRepository.findByTaskIdAndSectionId(taskId, sectionId);
    return new ResponseEntity<>(videoAddress, HttpStatus.OK);
  }
}
