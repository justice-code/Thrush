package org.eddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Justice-love on 2017/7/24.
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @RequestMapping(value = "/loginCaptcha", method = RequestMethod.GET.GET)
    public String captcha(@RequestParam("pipeline") String pipeline, Model model) {
        model.addAttribute("pipeline", "pipeline");
        return "login/captcha";
    }
}
