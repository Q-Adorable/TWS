package cn.thoughtworks.school.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/helper")
public class HelperController {

    @RequestMapping(value = "/healthy", method = RequestMethod.GET)
    public String getHealthy() {
        return "OK!";
    }
}