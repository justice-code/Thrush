package org.eddy.controller;

import org.eddy.pipeline.Pipeline;
import org.eddy.pipeline.command.Command;
import org.eddy.pipeline.command.CommandNotify;
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
@RequestMapping("/captcha")
public class CaptchaController {

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
}
