package cn.thoughtworks.school.services;


import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationCenterService {
    @Value("${notificationCenter}")
    private String notificationCenterUrl;
    private RestTemplate template = new RestTemplate();

    public void updateUnReadToReadById(Long notificationId) {
        String url = notificationCenterUrl + "/api/notifications/"+ notificationId+"?status=unRead";
         template.put(url, List.class);
    }

    public List getNotificationUnRead(Long userId) {
        String url = notificationCenterUrl + "/api/notifications/users/"+ userId+"?status=unRead";
        ResponseEntity<List> result = template.getForEntity(url, List.class);
        return result.getBody();
    }

    public void addNotification(Map data){
        String url = notificationCenterUrl + "/api/notifications";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(data, headers);
        template.exchange(url, HttpMethod.POST, requestEntity, Map.class);
    }
}
