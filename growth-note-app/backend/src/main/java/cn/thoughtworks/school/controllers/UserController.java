package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.entities.Follow;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.feign.UserCenterFeign;
import cn.thoughtworks.school.repositories.FollowRepository;
import cn.thoughtworks.school.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity getUserById(@Auth User user) {
        log.info(String.format("current user is: %s", user));
        Map result = userService.getDiaryAuthorInfo(user.getId());
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/followees/searching", method = RequestMethod.GET)
    public ResponseEntity searchingFollowees(@Auth User user, @RequestParam(value = "nameOrEmail") String nameOrEmail) {
        List<Map> users = userService.getUserByUserNameOrEmail(nameOrEmail);

        users.forEach(item ->{
            Long followeeId = Long.parseLong(item.get("id").toString());
            Follow existUser = followRepository.findByFollowerIdAndFolloweeId(user.getId(), followeeId);
            item.put("followed", existUser != null);
        });

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/followees/{followeeId}", method = RequestMethod.POST)
    public ResponseEntity createFollow(@Auth User user, @PathVariable Long followeeId) {
        Follow followee = followRepository.findByFollowerIdAndFolloweeId(user.getId(), followeeId);
        if (followee != null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        followee = new Follow();
        followee.setFollowerId(user.getId());
        followee.setFolloweeId(followeeId);
        followee.setCreateTime(simpleDateFormat.format(now));
        followee = followRepository.save(followee);
        return new ResponseEntity<>(followee.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/followees/{followeeId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFollow(@Auth User user, @PathVariable Long followeeId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(user.getId(), followeeId);
        if (follow != null) {
            followRepository.deleteById(follow.getId());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity getUsersLikeUsername(@RequestParam(value = "username") String username) {
        List result = userService.getUsersLikeUsername(username);
        return ResponseEntity.ok(result);
    }

}
