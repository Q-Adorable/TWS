package cn.thoughtworks.school.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(name="${feign.userCenter.name}",
        url = "${feign.userCenter.url}")
@Service
public interface UserCenterFeign {

    @GetMapping("/api/users/ids/{ids}")
    List getUsersByIds(@PathVariable("ids") String ids);

    @GetMapping("/api/users/{userId}")
    Map getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/api/users/query")
    List getUsersLikeUsername(@RequestParam("username") String username);

    @GetMapping("/api/users")
    List<Map> getUserByUserNameOrEmail(@RequestParam("nameOrEmail") String nameOrEmail);
}
