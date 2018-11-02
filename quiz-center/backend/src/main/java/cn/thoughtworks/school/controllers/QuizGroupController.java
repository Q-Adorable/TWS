package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.QuizGroup;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.entities.UserQuizGroup;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.QuizGroupRepository;
import cn.thoughtworks.school.repositories.UserQuizGroupRepository;
import cn.thoughtworks.school.services.Utils;
import cn.thoughtworks.school.services.UserCenterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v3/quizGroups")
@Slf4j
public class QuizGroupController {
    @Autowired
    private QuizGroupRepository quizGroupRepository;
    @Autowired
    private UserQuizGroupRepository userQuizGroupRepository;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private Utils utils;
    private ObjectMapper oMapper = new ObjectMapper();

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createQuizGroup(@RequestBody QuizGroup data, @Auth User user) {
        String time = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date());
        data.setCreateTime(time);
        data.setMakerId(user.getId());
        QuizGroup quizGroup = quizGroupRepository.save(data);
        UserQuizGroup userQuizGroup = new UserQuizGroup(user.getId(), quizGroup.getId(), time);
        userQuizGroupRepository.save(userQuizGroup);
        Map<String, Object> body = new HashMap<>();
        body.put("uri", "/api/v3/quizGroups/" + quizGroup.getId());
        body.put("id", quizGroup.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getQuizGroups(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pageSize", defaultValue = "10") Integer size, @Auth User current) {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        List<UserQuizGroup> userQuizGroupList = userQuizGroupRepository.findByUserId(current.getId());
        List<Long> groupIds = userQuizGroupList.stream().map(UserQuizGroup::getQuizGroupId).collect(Collectors.toList());
        Page quizGroups = quizGroupRepository.findAll(pageable);
        Page newQuizGroups = utils.formatExistUser(groupIds, quizGroups, pageable);
        Page results = utils.format(newQuizGroups, pageable);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }


    @RequestMapping(value = "/{quizGroupId}/users", method = RequestMethod.POST)
    public ResponseEntity joinQuizGroup(@PathVariable Long quizGroupId, @Auth User user) throws BusinessException {
        String time = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date());
        QuizGroup quizGroup = quizGroupRepository
                .findById(quizGroupId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown quizGroup with id: %s", quizGroupId)
                ));

        UserQuizGroup userExistGroup = userQuizGroupRepository.findByUserIdAndQuizGroupId(user.getId(), quizGroupId);
        if (userExistGroup != null) {
            throw new BusinessException("该用户已在该题组，不能删除");
        }
        UserQuizGroup userQuizGroup = new UserQuizGroup(user.getId(), quizGroupId, time);
        userQuizGroupRepository.save(userQuizGroup);
        Map<String, Object> body = new HashMap<>();
        body.put("uri", "/api/v3/quizGroups/" + quizGroupId + "/users/" + user.getId());
        body.put("id", userQuizGroup.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/myQuizGroups", method = RequestMethod.GET)
    public ResponseEntity getAllMyQuizGroups(@Auth User current) {
        List<UserQuizGroup> userQuizGroupList = userQuizGroupRepository.findByUserId(current.getId());
        List groupIds = userQuizGroupList.stream().map(UserQuizGroup::getQuizGroupId).collect(Collectors.toList());
        List quizGroups = quizGroupRepository.findByIdIn(groupIds);
        return new ResponseEntity<>(quizGroups, HttpStatus.OK);
    }

    @RequestMapping(value = "/{quizGroupId}", method = RequestMethod.GET)
    public ResponseEntity getQuizGroup(@PathVariable Long quizGroupId) throws BusinessException {
        QuizGroup quizGroup = quizGroupRepository
                .findById(quizGroupId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown quizGroup with id: %s", quizGroupId)
                ));

        return new ResponseEntity<>(quizGroup, HttpStatus.OK);
    }

    @RequestMapping(value = "/{quizGroupId}/users", method = RequestMethod.GET)
    public ResponseEntity getUsersFromQuizGroup(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pageSize", defaultValue = "10") Integer size, @PathVariable Long quizGroupId) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        Page<UserQuizGroup> userQuizGroupList = userQuizGroupRepository.findByQuizGroupId(quizGroupId, pageable);
        String userIds = userQuizGroupList.getContent().stream().map(userQuizGroup -> userQuizGroup.getUserId().toString()).collect(Collectors.joining(","));
        List userList = userCenterService.getUsersByIds(userIds);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{quizGroupId}", method = RequestMethod.PUT)
    public ResponseEntity updateQuizGroup(@PathVariable Long quizGroupId, @RequestBody Map data) throws BusinessException {
        QuizGroup quizGroup = quizGroupRepository
                .findById(quizGroupId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown quizGroup with id: %s", quizGroupId)
                ));

        String time = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date());
        quizGroup.setModifyTime(time);
        quizGroup.setName((String) data.get("name"));
        quizGroup.setDescription((String) data.get("description"));
        quizGroupRepository.save(quizGroup);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{quizGroupId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUserFromQuizGroup(@PathVariable Long quizGroupId, @Auth User current) throws BusinessException {
        UserQuizGroup userQuizGroup = userQuizGroupRepository.findByUserIdAndQuizGroupId(current.getId(), quizGroupId);
        if (userQuizGroup == null) {
            throw new BusinessException("学员不在该题组中,后台错误");
        }
        userQuizGroupRepository.deleteById(userQuizGroup.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{quizGroupId}/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUserFromQuizGroup(@PathVariable Long quizGroupId, @PathVariable Long userId) throws BusinessException {
        UserQuizGroup userQuizGroup = userQuizGroupRepository.findByUserIdAndQuizGroupId(userId, quizGroupId);
        if (userQuizGroup == null) {
            throw new BusinessException("学员不在该题组中,后台错误");
        }
        userQuizGroupRepository.deleteById(userQuizGroup.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}



