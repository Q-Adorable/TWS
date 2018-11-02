package cn.thoughtworks.school.programCenter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient(name = "${feign.userCenter.name}",
        url = "${feign.userCenter.url}")
@Service
public interface UserCenterFeign {

    @GetMapping("api/users/ids/{ids}")
    ResponseEntity<List<Map>> getUsersByIds(@PathVariable("ids") String ids);

    @GetMapping("api/users/{userId}")
    ResponseEntity<Map> getUserById(@PathVariable("userId") Long userId);

    @GetMapping("api/users/username")
    ResponseEntity<Map> getUserByUsername(@RequestParam("username") String username);

    @GetMapping("api/users")
    ResponseEntity<List<Map>> getUserByUserNameOrEmail(@RequestParam("nameOrEmail") String nameOrEmail);

    @PostMapping("api/users/roles")
    ResponseEntity addTutorRole(@RequestBody Map data);

    @PostMapping("api/users/searches")
    ResponseEntity<List<Map>> searchUsersByConditions(@RequestBody Map searchParam);
}
