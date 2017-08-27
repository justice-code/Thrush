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

    @RequestMapping(path = "/login", method = RequestMethod.POST)
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
        return refreshCaptcha(pipeline, Command.REFRESH_LOGIN_CAPTCHA);
    }

    @RequestMapping(path = "/confirmRefresh", method = RequestMethod.GET)
    public ResponseEntity<Void> refreshTicketCaptcha(String pipeline) {
        return refreshCaptcha(pipeline, Command.TICKET_CAPTCHA);
    }

    private ResponseEntity<Void> refreshCaptcha(String pipeline, Command command) {
        Objects.requireNonNull(pipeline);
        Objects.requireNonNull(command);

        CommandNotify notify = new CommandNotify();
        notify.setPipeline(pipeline);
        notify.setArg(CaptchaUtil.imageFileName());
        notify.setCommand(command);

        Pipeline.putNotify(notify);
        return ResponseEntity.ok().build();
    }
}
