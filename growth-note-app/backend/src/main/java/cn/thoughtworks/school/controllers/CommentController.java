package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.entities.Comment;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/comments")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> createComment(@RequestBody Comment comment, @Auth User user) {

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        comment.setCommentAuthorId(user.getId());
        comment.setCommentTime(simpleDateFormat.format(now));
        comment = commentRepository.save(comment);
        Map<String, String> body = new HashMap<>();
        body.put("uri", "/api/users/" + user.getId() + "/comments/" + comment.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }
}
