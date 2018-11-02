package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.TaskFelling;
import cn.thoughtworks.school.programCenter.entities.User;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.TaskFeelingRepository;
import cn.thoughtworks.school.programCenter.repositories.TaskSuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/api")
public class TaskFeelingController {

    @Autowired
    private TaskFeelingRepository taskFeelingRepository;

    @RequestMapping(value = "/feelings", method = RequestMethod.POST)
    public ResponseEntity addTasksSuggestion(@Auth User current, @RequestBody TaskFelling taskFeeling) throws Exception {
        TaskFelling currentTaskFelling = taskFeelingRepository.findByTaskIdAndStudentId(taskFeeling.getTaskId(), current.getId());
        if (currentTaskFelling != null) {
            throw new BusinessException("您已经评价过了");
        }
        taskFeeling.setStudentId(current.getId());
        TaskFelling taskFelling = taskFeelingRepository.save(taskFeeling);
        Map<String, String> body = new HashMap<>();
        body.put("uri", "/api/feelings/" + taskFelling.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/tasks/{taskId}/feelings", method = RequestMethod.GET)
    public ResponseEntity getTasksFeelings(@Auth User current, @PathVariable Long taskId) throws Exception {
        TaskFelling taskFelling = taskFeelingRepository.findByTaskIdAndStudentId(taskId, current.getId());
        if (taskFelling == null) {
            taskFelling = new TaskFelling();
        }
        return new ResponseEntity<>(taskFelling, HttpStatus.OK);
    }

}
