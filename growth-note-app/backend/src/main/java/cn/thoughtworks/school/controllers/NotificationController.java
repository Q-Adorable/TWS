package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.services.NotificationCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationCenterService notificationCenterService;

    @GetMapping(value = "")
    public ResponseEntity getNotificationUnRead(@Auth User user) {
        List<Notification> notificationNoRead = notificationCenterService.getNotificationUnRead(user.getId());
        return new ResponseEntity(notificationNoRead, HttpStatus.OK);
    }

    @PutMapping(value = "/{notificationId}")
    public ResponseEntity updateUnReadToReadById(@PathVariable Long notificationId) {
        notificationCenterService.updateUnReadToReadById(notificationId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "")
    public ResponseEntity addNotification(@RequestBody Map data, @Auth User user) {
        data.put("senderId", user.getId());
        data.put("followees", user.getId());
        notificationCenterService.addNotification(data);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
