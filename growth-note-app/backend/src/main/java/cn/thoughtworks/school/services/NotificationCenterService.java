package cn.thoughtworks.school.services;


import cn.thoughtworks.school.feign.NotificationCenterFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
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
