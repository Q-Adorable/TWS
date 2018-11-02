package cn.thoughtworks.school.resolvers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.controllers.UserController;
import cn.thoughtworks.school.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthResolver implements HandlerMethodArgumentResolver {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Auth.class) != null;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        log.info(String.format("current user is: %s", webRequest.getHeader("id")));
        log.info(String.format("current user roles: %s", webRequest.getHeader("roles")));

        String username = webRequest.getHeader("username");
        String roleStr = webRequest.getHeader("roles");
        String id = webRequest.getHeader("id");
        User current = new User();
        if(roleStr == null || "".equals(roleStr)) {
            roleStr = "0";
        }

        List<Integer> roles = Arrays.stream(roleStr.split(","))
                .map(item-> Integer.valueOf(item))
                .collect(Collectors.toList());
        current.setUsername(username);
        current.setRoles(roles);
        if (id != null) {
            current.setId(Long.valueOf(id));
        }

        log.info(String.format("result is: %s", current.toString()));
        return current;
    }
}
