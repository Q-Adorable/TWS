package cn.thoughtworks.school.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class SettingsController {

    @Value("${appContextPath}")
    private String appContextPath;

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public Map<String, String> getSettings() {
        Map<String, String> result = new HashMap<>();

        result.put("appContextPath", appContextPath);
        return result;
    }

}
