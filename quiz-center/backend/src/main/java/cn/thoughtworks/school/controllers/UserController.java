package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.services.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {

  @Autowired
  private UserCenterService userCenterService;

  @RequestMapping(value = "/v3/users", method = RequestMethod.GET)
  public ResponseEntity searchUsers (@RequestParam(value = "userName") String userName) {
    return userCenterService.getUsersByUsername(userName);
  }

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
  public Object getUserById(@PathVariable Long userId) {
    return userCenterService.getUserInfoById(userId);
  }
}
