package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.repositories.TagRepository;
import cn.thoughtworks.school.programCenter.services.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/programs")
public class UserTagController {
    @Autowired
    private UserTagService userTagService;
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @PutMapping(value = "{programId}/students/{studentId}/tags")
    public ResponseEntity editSudentTags(@PathVariable Long programId,
                                         @PathVariable Long studentId,
                                         @Auth User current,
                                         @RequestBody Map data) {
        List<String> tags = (List<String>) data.get("tags");
        userTagService.editUserTags(programId, studentId, current.getId(), tags);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("tags")
    public ResponseEntity getTags() {
        return new ResponseEntity(tagRepository.findAll(),HttpStatus.OK);
    }
}
