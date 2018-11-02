package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.TaskFelling;
import cn.thoughtworks.school.programCenter.entities.TaskSuggestion;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.TaskSuggestionRepository;
import cn.thoughtworks.school.programCenter.services.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/api/tasks")
public class TaskSuggestionController {
    @Autowired
    private TaskSuggestionRepository taskSuggestionRepository;

    @Autowired
    private UserCenterService centerService;

    @RequestMapping(value = "/{taskId}/suggestions", method = RequestMethod.POST)
    public ResponseEntity addTasksSuggestionContent(@PathVariable Long taskId, @Auth User current, @RequestBody String content) throws Exception {

        TaskSuggestion taskSuggestion = new TaskSuggestion();
        taskSuggestion.setStudentId(current.getId());
        taskSuggestion.setTaskId(taskId);
        taskSuggestion.setContent(content);
        taskSuggestion.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        taskSuggestionRepository.save(taskSuggestion);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{taskId}/suggestions", method = RequestMethod.GET)
    public ResponseEntity getTasksSuggestionByTaskId(@PathVariable Long taskId) {

        List<TaskSuggestion> taskSuggestions = taskSuggestionRepository.findByTaskId(taskId);
        ArrayList<HashMap> suggestionsInfoList = new ArrayList<>();
        for (TaskSuggestion taskSuggestion : taskSuggestions) {

            HashMap suggestionInfo = new HashMap<>();
            ResponseEntity userInfo = centerService.getUserInfo(taskSuggestion.getStudentId());
            suggestionInfo.put("taskSuggestion", taskSuggestion);
            suggestionInfo.put("studentInfo", userInfo.getBody());
            suggestionsInfoList.add(suggestionInfo);
        }
        return new ResponseEntity<>(suggestionsInfoList, HttpStatus.OK);
    }

}
