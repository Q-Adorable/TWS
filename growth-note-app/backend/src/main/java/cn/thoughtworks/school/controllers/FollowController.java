package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.entities.Follow;
import cn.thoughtworks.school.entities.PractiseDiary;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.repositories.FollowRepository;
import cn.thoughtworks.school.repositories.PractiseDiaryRepository;
import cn.thoughtworks.school.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/followees")
public class FollowController {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private PractiseDiaryRepository practiseDiaryRepository;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getContactList(@Auth User user) {
        List<Follow> follows = followRepository.findByFollowerId(user.getId());

        List<Map> diariesAndContactInfoList = new ArrayList();
        for (Follow follow : follows) {
            HashMap<String, Object> diariesAndContactInfo = new HashMap();
            List<PractiseDiary> practiseDiaryList = practiseDiaryRepository.findByAuthorIdOrderByDateDesc(follow.getFolloweeId());
            Map userInfo = userService.getDiaryAuthorInfo(follow.getFolloweeId());
            diariesAndContactInfo.put("practiseDiaryList", practiseDiaryList);
            diariesAndContactInfo.put("userInfo", userInfo);
            diariesAndContactInfoList.add(diariesAndContactInfo);
        }
        return new ResponseEntity<>(diariesAndContactInfoList, HttpStatus.OK);
    }


    @RequestMapping(value = "/{followeeId}/practise-diaries", method = RequestMethod.GET)
    public ResponseEntity getContactUserDiariesAndComments(@Auth User user, @PathVariable Long followeeId, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pageSize", defaultValue = "20") Integer size) {

        Follow follow = followRepository.findByFollowerIdAndFolloweeId(user.getId(), followeeId);
        if (follow == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "Date");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        List<PractiseDiary> practiseDiaryList = practiseDiaryRepository.findByAuthorId(followeeId, pageable);
        List practiseAndCommentsList = userService.attachComments(practiseDiaryList);
        Map followeeInfo = userService.getDiaryAuthorInfo(followeeId);
        HashMap<String, Object> practiseAndComments = new HashMap();
        practiseAndComments.put("total", (practiseDiaryRepository.findByAuthorIdOrderByDateDesc(followeeId)).size());
        practiseAndComments.put("practiseDiaryAndComments", practiseAndCommentsList);
        practiseAndComments.put("followeeInfo", followeeInfo);
        return new ResponseEntity<>(practiseAndComments, HttpStatus.OK);
    }

    @RequestMapping(value = "/tutors", method = RequestMethod.GET)
    public ResponseEntity getFollowTutors(@Auth User user) {
        List<Follow> follows = followRepository.findByFolloweeId(user.getId());
        List<Long> followerIds = follows.stream().map(Follow::getFollowerId).collect(Collectors.toList());
        List followers = userService.getUsersByIds(followerIds);

        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

}
