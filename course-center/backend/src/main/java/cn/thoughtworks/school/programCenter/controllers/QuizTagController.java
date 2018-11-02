package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.services.QuizCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/api/v2")
public class QuizTagController {

  @Autowired
  private QuizCenterService quizCenterService;

  @RequestMapping(value = "/tags", method = RequestMethod.GET)
  public ResponseEntity getTags() {
    return quizCenterService.getTags();
  }
}
