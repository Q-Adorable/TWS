package cn.thoughtworks.school.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient(name="${feign.notificationCenter.name}",
        url = "${feign.notificationCenter.url}")
@Service
public interface NotificationCenterFeign {

    @GetMapping("api/notifications/users/{userId}")
    ResponseEntity<List> getNotificationUnRead(@PathVariable("userId") Long userId, @RequestParam("status") String status);

    @PutMapping("api/notifications/{notificationId}")
    ResponseEntity<List> updateUnReadToReadById(@PathVariable("notificationId") Long notificationId, @RequestParam("status") String status);

    @PostMapping("api/notifications")
    ResponseEntity<List> addNotification(@RequestBody Map map);
}
