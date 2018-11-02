package cn.thoughtworks.school.programCenter.services;

import cn.thoughtworks.school.programCenter.entities.Tag;
import cn.thoughtworks.school.programCenter.entities.UserTag;
import cn.thoughtworks.school.programCenter.feign.UserCenterFeign;
import cn.thoughtworks.school.programCenter.repositories.UserTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserCenterService {
    private final static Integer TUTOR_ROLE = 2;

    @Autowired
    private UserTagRepository userTagRepository;
    @Autowired
    private UserCenterFeign userCenterFeign;

    public ResponseEntity getUserInfo(Long userId) {
        return userCenterFeign.getUserById(userId);
    }

    public List getUsersByIds(String ids) {
        if (ids.equals("")) {
            return new ArrayList();
        }
        List<Map> users = userCenterFeign.getUsersByIds(ids).getBody();
        return users.stream().map(item -> {
            item.remove("password");
            return item;
        }).collect(Collectors.toList());
    }

    public ResponseEntity<List<Map>> getUserByNameOrEmail(String nameOrEmail) {
        return userCenterFeign.getUserByUserNameOrEmail(nameOrEmail);

    }

    public Map getUserByName(String name) {
        return userCenterFeign.getUserByUsername(name).getBody();
    }

    public void addTutorRole(Long tutorId) {
        Map data = new HashMap();
        data.put("userId", tutorId);
        data.put("role", TUTOR_ROLE);

        userCenterFeign.addTutorRole(data);
    }

    public List<Map> getUsersByUsernameOrEmail(List usernameOrEmails) {
        Map condition = new HashMap();
        condition.put("usernameOrEmail", usernameOrEmails);

        return userCenterFeign.searchUsersByConditions(condition).getBody();
    }

    public List getUsers(String studentIds, Long programId) {
        List result = new ArrayList();
        List<Map> users = getUsersByIds(studentIds);
        users.forEach(item -> {
            Map temp = new HashMap();
            temp.put("id", item.get("id"));
            temp.put("name", item.get("name"));
            temp.put("mobilePhone", item.get("mobilePhone"));
            temp.put("email", item.get("email"));
            temp.put("username", item.get("username"));
            List<Tag> tags = userTagRepository.findByProgramIdAndStudentId(programId, Long.valueOf(item.get("id").toString()))
                    .stream().map(UserTag::getTag).collect(Collectors.toList());
            temp.put("tags", tags);
            result.add(temp);
        });

        return result;
    }
}
