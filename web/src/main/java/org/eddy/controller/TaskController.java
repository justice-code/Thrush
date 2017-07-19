package org.eddy.controller;

import org.eddy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Justice-love on 2017/7/18.
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(path = "/begin", method = RequestMethod.GET)
    public ResponseEntity<String> begin() {
        String group = taskService.submit();
        return ResponseEntity.ok(group);
    }
}
