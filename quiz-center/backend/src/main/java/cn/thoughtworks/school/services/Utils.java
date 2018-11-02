package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.BasicQuizSubmission;
import cn.thoughtworks.school.entities.QuizGroup;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.entities.Tag;
import cn.thoughtworks.school.repositories.StackRepository;
import cn.thoughtworks.school.repositories.TagRepository;
import cn.thoughtworks.school.repositories.QuizGroupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Utils {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private QuizGroupRepository quizGroupRepository;

    private ObjectMapper oMapper = new ObjectMapper();

    public Set<Tag> formatTags(List<String> tagList, Long userId) {
        return tagList.stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findTagByName(tagName).findFirst()
                            .orElse(new Tag(tagName, userId, 1L));


                    return tag;
                })
                .collect(Collectors.toSet());
    }


    public Map formatAddTypeToQuiz(Object object, String type) {
        if (object == null) {
            return null;
        }
        Map<String, Object> map = oMapper.convertValue(object, Map.class);

        map.put("type", type);

        return map;
    }

    public Map formatEntry(Object object, List<Map> users) {
        Map<String, Object> map = oMapper.convertValue(object, Map.class);
        if (map.get("makerId") != null) {
            map.put("maker", getQuizMaker(users, map));
        } else {
            map.put("maker", "");
        }
        if (map.get("quizGroupId") != null) {
            QuizGroup quizGroup = quizGroupRepository
                    .findById((Long) map.get("quizGroupId"))
                    .orElse(new QuizGroup());

            map.put("quizGroupName", quizGroup.getName());

        }
        if (map.get("stackId") != null) {
            Stack stack = stackRepository
                    .findById((Long) map.get("stackId"))
                    .orElse(new Stack());

            map.put("stack", stack.getTitle());
        }
        return map;
    }


    public Page formatExistUser(List<Long> ids, Page quizGroups, Pageable pageable) {
        List<QuizGroup> content = quizGroups.getContent();
        List newContent = content.stream().map((quizGroup) -> {
            Map<String, Object> map = oMapper.convertValue(quizGroup, Map.class);
            if (ids.contains(quizGroup.getId())) {
                map.put("userIsExist", true);
            }
            return map;
        }).collect(Collectors.toList());
        return new PageImpl<>(newContent, pageable, quizGroups.getTotalElements());
    }


    public Page format(Page formatPage, Pageable pageable) {
        List content = formatPage.getContent();
        List<Map> newContent = formatList(content);
        return new PageImpl<>(newContent, pageable, formatPage.getTotalElements());
    }

    public List<Map> formatList(List list) {

        String ids = getMakerIds(list);

        List<Map> users = userCenterService.getUsersByIds(ids);

        return (List<Map>) list.stream().map(format -> formatEntry(format, users)).collect(Collectors.toList());
    }

    public String getMakerIds(List list) {
        return (String) list.stream()

                .map(item -> {
                    Map<String, Object> map = oMapper.convertValue(item, Map.class);
                    if (map.get("makerId") == null) {
                        return "";
                    }
                    return map.get("makerId").toString();
                })
                .distinct()
                .filter(str -> !str.equals(""))
                .collect(Collectors.joining(","));

    }

    public String getQuizMaker(List<Map> users, Map map) {
        Optional<Map> optional = users.stream()

                .filter(u -> u.get("id").toString().equals(map.get("makerId").toString()))
                .findFirst();

        String userName = "";
        if (optional.isPresent()) {
            userName = (String) optional.get().get("username");
        }

        return userName;
    }

    public Map addUserAnswerWithBasicQuiz(Object object, List<BasicQuizSubmission> basicQuizSubmissions) {
        Map map = oMapper.convertValue(object, Map.class);
        Optional<BasicQuizSubmission> BasicQuizSubmissionOptional = basicQuizSubmissions.stream()

                .filter(basicQuizSubmission -> basicQuizSubmission.getQuizId().toString().equals(map.get("id").toString()))
                .findFirst();

        if (BasicQuizSubmissionOptional.isPresent()) {
            map.put("userAnswer", BasicQuizSubmissionOptional.get().getUserAnswer());
            map.put("isCorrect", BasicQuizSubmissionOptional.get().getCorrect());
        } else {
            map.put("userAnswer", "");
            map.put("isCorrect", "");
        }
        return map;
    }

    public Map addUserAnswer(Object object, List quizSubmissions) {
        Map map = oMapper.convertValue(object, Map.class);
        Optional quizSubmissionOptional = quizSubmissions.stream()
                .filter(quizSubmission -> {
                    Map quizSubmissionMap = oMapper.convertValue(quizSubmission, Map.class);
                    return quizSubmissionMap.get("quizId").toString().equals(map.get("id").toString());
                }).findFirst();
        if (quizSubmissionOptional.isPresent()) {
            Map quizSubmissionMap = oMapper.convertValue(quizSubmissionOptional.get(), Map.class);
            map.put("userAnswer", quizSubmissionMap.get("userAnswer"));
            map.put("answerBranch", quizSubmissionMap.get("answerBranch"));
        } else {
            map.put("userAnswer", "");
            map.put("answerBranch", "");
        }
        return map;
    }

    public String readFileToString(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileContent = IOUtils.toString(inputStream, "utf-8");
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
