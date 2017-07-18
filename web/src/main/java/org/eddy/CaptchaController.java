package org.eddy;

import org.eddy.pipeline.LoginPipeline;
import org.eddy.solve.CaptchaNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Autowired
    private LoginPipeline loginPipeline;

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> loginCaptcha(CaptchaNotify captchaResult) {
        Objects.requireNonNull(captchaResult);
        loginPipeline.putNotify(captchaResult);
        return ResponseEntity.ok().build();
    }
}
