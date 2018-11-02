package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.entities.StackCommand;
import cn.thoughtworks.school.services.StackCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author yywei
 **/
@RestController
@RequestMapping(value = "/api/v3/stackCommand")
public class StackCommandController {
    @Autowired
    private StackCommandService stackCommandService;


    @PostMapping
    public ResponseEntity addStackCommand(@RequestBody StackCommand stackCommand) {
        StackCommand save = stackCommandService.save(stackCommand);
        if (save == null)
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/api/v3/stackCommand/{id}")
    public ResponseEntity deleteStackCommand(@PathVariable Long id) {
        stackCommandService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
