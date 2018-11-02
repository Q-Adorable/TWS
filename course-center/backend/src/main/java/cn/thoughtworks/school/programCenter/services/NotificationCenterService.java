package cn.thoughtworks.school.programCenter.services;


import cn.thoughtworks.school.programCenter.feign.NotificationCenterFeign;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private NotificationCenterFeign notificationCenterFeign;

    public void updateUnReadToReadById(Long notificationId) {
        notificationCenterFeign.updateUnReadToReadById(notificationId, "unRead");
    }

    public List getNotificationUnRead(Long userId) {
        return notificationCenterFeign.getNotificationUnRead(userId, "unRead").getBody();
    }

    public void addNotification(Map data){
        notificationCenterFeign.addNotification(data);
    }
}
