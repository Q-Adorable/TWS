package cn.thoughtworks.school.programCenter.resolvers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Auth.class) != null;
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
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
        return current;
    }
}
