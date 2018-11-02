package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserCenterService {

    @Value("${userCenterUrl}")
    private String userCenterUrl;

    public Object getUserInfoById(Long userId) {
        if (userId == null) {
            return new User();
        }
        String getUsersUrl = userCenterUrl + "/api/users/" + userId;
        RestTemplate template = new RestTemplate();
        ResponseEntity<Map> result = template.getForEntity(getUsersUrl, Map.class);
        return result.getBody();
    }

    public ResponseEntity getUsersByUsername(String username) {
        String getUsersUrl = userCenterUrl + "/api/users?nameOrEmail=" + username;
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> result = template.getForEntity(getUsersUrl, String.class);
        return result;
    }

    public List getUsersByIds(String ids) {

        if (ids.equals("")) {
            return new ArrayList();
        }

        String getUsersUrl = userCenterUrl + "/api/users/ids/" + ids;
        RestTemplate template = new RestTemplate();
        ResponseEntity<List> result = template.getForEntity(getUsersUrl, List.class);
        return result.getBody();
    }

    @SuppressWarnings("unused")
    private List fallback(String goodsId) {
        return new ArrayList();
    }
}
