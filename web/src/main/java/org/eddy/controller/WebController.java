package org.eddy.controller;

import org.eddy.pipeline.Pipeline;
import org.eddy.pipeline.command.Command;
import org.eddy.pipeline.command.CommandNotify;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/24.
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @RequestMapping(value = "/loginCaptcha", method = RequestMethod.GET)
    public String captcha(@RequestParam("pipeline") String pipeline, Model model) {
        model.addAttribute("pipeline", pipeline);
        return "login/captcha";
    }

    @RequestMapping(value = "/tranBegin", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Void> tranBegin(@RequestParam("pipeline") String pipeline) {
        Objects.requireNonNull(pipeline);

        CommandNotify notify = new CommandNotify();
        notify.setPipeline(pipeline);
        notify.setCommand(Command.START_CHOOSE_TRAIN);

        Pipeline.putNotify(notify);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/train", method = RequestMethod.GET)
    public String train(@RequestParam("pipeline") String pipeline, @RequestParam("passengers") String[] passengers, Model model) {
        Objects.requireNonNull(pipeline);

        model.addAttribute("pipeline", pipeline);
        model.addAttribute("passengers", passengers);

        return "train/train";
    }
}
