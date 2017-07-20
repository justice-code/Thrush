package org.eddy.controller;

import org.eddy.captcha.CaptchaUtil;
import org.eddy.pipeline.Pipeline;
import org.eddy.pipeline.command.Command;
import org.eddy.pipeline.command.CommandNotify;
import org.eddy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public ResponseEntity<Void> loginCaptcha(String pipeline, Integer[] numbers) {
        Objects.requireNonNull(pipeline);
        Objects.requireNonNull(numbers);

        CommandNotify notify = new CommandNotify();
        notify.setPipeline(pipeline);
        notify.setArg(numbers);
        notify.setCommand(Command.LOGIN);

        Pipeline.putNotify(notify);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<Void> refreshLoginCaptcha(String pipeline) {
        Objects.requireNonNull(pipeline);

        CommandNotify notify = new CommandNotify();
        notify.setPipeline(pipeline);
        notify.setArg(CaptchaUtil.imageFileName());
        notify.setCommand(Command.REFRESH_LOGIN_CAPTCHA);

        Pipeline.putNotify(notify);
        return ResponseEntity.ok().build();
    }
}
