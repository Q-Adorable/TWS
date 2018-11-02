package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.entities.ExcellentDiary;
import cn.thoughtworks.school.entities.PractiseDiary;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.repositories.ExcellentDiaryRepository;
import cn.thoughtworks.school.repositories.PractiseDiaryRepository;
import cn.thoughtworks.school.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/diaries")
public class PractiseDiaryController {
    @Autowired
    private PractiseDiaryRepository practiseDiaryRepository;
    @Autowired
    private ExcellentDiaryRepository excellentDiaryRepository;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<HashMap> getByAuthorId(@Auth User user, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pageSize", defaultValue = "20") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "Date");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        List<PractiseDiary> practiseDiaries = practiseDiaryRepository.findByAuthorId(user.getId(), pageable);

        List practiseAndCommentsList = userService.attachComments(practiseDiaries);
        HashMap<String, Object> practiseAndComments = new HashMap();
        practiseAndComments.put("total", (practiseDiaryRepository.findByAuthorIdOrderByDateDesc(user.getId())).size());
        practiseAndComments.put("practiseDiaryAndComments", practiseAndCommentsList);

        Map authorInfo = userService.getDiaryAuthorInfo(user.getId());
        practiseAndComments.put("userInfo", authorInfo);
        return new ResponseEntity<>(practiseAndComments, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> createDiary(@Auth User user, @RequestBody PractiseDiary practiseDiary) {

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        practiseDiary.setAuthorId(user.getId());
        practiseDiary.setCreateTime(simpleDateFormat.format(now));
        practiseDiary = practiseDiaryRepository.save(practiseDiary);
        Map<String, String> body = new HashMap<>();
        body.put("uri", "/api/users/" + user.getId() + "/diaries/" + practiseDiary.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDiary(@PathVariable long id) {
        if (practiseDiaryRepository.existsById(id)) {
            ExcellentDiary excellentDiary = excellentDiaryRepository.findByDiaryId(id);
            if (excellentDiary != null) {
                excellentDiaryRepository.deleteById(excellentDiary.getId());
            }
            practiseDiaryRepository.deleteById(id);
            return new ResponseEntity<>(practiseDiaryRepository.findById(id), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateDiary(@PathVariable Long id, @RequestBody PractiseDiary practiseDiary) {
        PractiseDiary currentPractiseDiary = practiseDiaryRepository.findById(id).get();
        if (currentPractiseDiary == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        currentPractiseDiary.setContent(practiseDiary.getContent());
        currentPractiseDiary.setDate(practiseDiary.getDate());
        currentPractiseDiary = practiseDiaryRepository.save(currentPractiseDiary);
        return new ResponseEntity<>(currentPractiseDiary.getId(), HttpStatus.NO_CONTENT);
    }
}
