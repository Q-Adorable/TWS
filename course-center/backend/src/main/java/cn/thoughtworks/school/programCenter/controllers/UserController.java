package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.services.ProgramService;
import cn.thoughtworks.school.programCenter.services.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    private final static String SEARCH_STUDENT_PROGRAMS = "student";

    @Autowired
    private ProgramService programService;
    @Autowired
    private UserCenterService userCenterService;

    @GetMapping("/users/{userId}")
    public ResponseEntity getUserById(@PathVariable Long userId) {
        return userCenterService.getUserInfo(userId);
    }

    @GetMapping("/v2/users")
    public ResponseEntity getUsersByUsername(@RequestParam(value = "userName", defaultValue = "") String userName) {
        Map user = userCenterService.getUserByName(userName);

        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/users/searches")
    public ResponseEntity searchUsersByCondition(@RequestBody Map condition) {
        List usernameOrEmails = (List) condition.get("usernameOrEmail");
        String userType = condition.get("type").toString();
        List<Map> searchUsers = userCenterService.getUsersByUsernameOrEmail(usernameOrEmails);

        if (Objects.equals(userType, SEARCH_STUDENT_PROGRAMS)) {
            List list = programService.searchStudentsPrograms(searchUsers);
            return new ResponseEntity(list, HttpStatus.OK);
        }

        return new ResponseEntity(programService.searchTutorsPrograms(searchUsers), HttpStatus.OK);
    }
}
