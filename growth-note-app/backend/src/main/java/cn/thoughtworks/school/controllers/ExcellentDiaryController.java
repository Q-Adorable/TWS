package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.entities.Comment;
import cn.thoughtworks.school.entities.ExcellentDiary;
import cn.thoughtworks.school.entities.PractiseDiary;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.repositories.CommentRepository;
import cn.thoughtworks.school.repositories.ExcellentDiaryRepository;
import cn.thoughtworks.school.repositories.PractiseDiaryRepository;
import cn.thoughtworks.school.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/excellentDiaries")
public class ExcellentDiaryController {
    @Autowired
    private ExcellentDiaryRepository excellentDiaryRepository;

    @Autowired
    private PractiseDiaryRepository practiseDiaryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> createExcellentDiary(@Auth User user, @RequestBody ExcellentDiary excellentDiary) {

        ExcellentDiary existDiary = excellentDiaryRepository.findByDiaryId(excellentDiary.getDiaryId());
        if (existDiary != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        excellentDiary.setOperatorId(user.getId());
        excellentDiary.setCreateTime(simpleDateFormat.format(now));
        excellentDiary = excellentDiaryRepository.save(excellentDiary);
        Map<String, String> body = new HashMap<>();
        body.put("uri", "/api/users/" + user.getId() + "/excellentDiaries/" + excellentDiary.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{excellentDiaryId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> deleteExcellentDiary(@PathVariable Long excellentDiaryId) {
        ExcellentDiary existDiary = excellentDiaryRepository.findByDiaryId(excellentDiaryId);
        if (existDiary == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        excellentDiaryRepository.deleteById(existDiary.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<HashMap> getAllExcellentDiaries(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pageSize", defaultValue = "20") Integer size) {
        List excellentDiariesAndComments = new ArrayList();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, size, sort);

        Page<ExcellentDiary> excellentDiaryPage = excellentDiaryRepository.findAll(pageable);
        List<Long> diaryIds = excellentDiaryPage.getContent().stream().map(ExcellentDiary::getDiaryId).collect(Collectors.toList());
        List<PractiseDiary> excellentDiaries = practiseDiaryRepository.findAllByIdIn(diaryIds);

        List<Long> userIds = excellentDiaries.stream().map(PractiseDiary::getAuthorId).collect(Collectors.toList());
        List<Map> users = userService.getUsersByIds(userIds);
        System.out.println(users.size()+"：excellence count");
        excellentDiaries.forEach(diary ->{
            List<Comment> comments = commentRepository.findByPractiseDiaryId(diary.getId());

            Map diaryAuthorInfo = users.stream().filter(item ->
                    Objects.equals(Long.valueOf(item.get("id").toString()), diary.getAuthorId()))
                    .findFirst()
                    .orElse(new HashMap());

            List commentsInfo = userService.getCommonInfo(comments);
            HashMap<String, Object> excellentPractiseDiary = new HashMap();
            excellentPractiseDiary.put("excellentDiary", diary);
            excellentPractiseDiary.put("comments", commentsInfo);
            excellentPractiseDiary.put("diaryAuthorInfo", diaryAuthorInfo);
            excellentDiariesAndComments.add(excellentPractiseDiary);
        });


        HashMap<String, Object> excellentDiariesInfo = new HashMap();
        excellentDiariesInfo.put("total", excellentDiaryRepository.findAll().size());
        excellentDiariesInfo.put("excellentDiariesAndComments", excellentDiariesAndComments);
        return new ResponseEntity<>(excellentDiariesInfo, HttpStatus.OK);
    }
}
