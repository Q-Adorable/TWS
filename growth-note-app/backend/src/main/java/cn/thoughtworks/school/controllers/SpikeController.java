package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotation.Auth;
import cn.thoughtworks.school.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/spike")
public class SpikeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> get() {
        Map<String, String> body = new HashMap<>();
        body.put("uri", "OK");
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> post(@Auth User auth) {

        Map<String, String> body = new HashMap<>();
        SpikeController.class.getAnnotation(Auth.class);
        String result = getClassAnnotationValue(SpikeController.class, Auth.class, "path");
        body.put("value", auth.getUsername());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    public String getClassAnnotationValue(Class classType, Class annotationType, String attributeName) {
        String value = null;

        Annotation annotation = classType.getAnnotation(annotationType);
        if (annotation != null) {
            try {
                value = (String) annotation.annotationType().getMethod(attributeName).invoke(annotation);
            } catch (Exception ex) {
            }
        }

        return value;
    }
}



