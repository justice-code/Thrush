package org.eddy;

import org.eddy.web.CaptchaResult;
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

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> loginCaptcha(CaptchaResult captchaResult) {
        Objects.requireNonNull(captchaResult);
        System.out.println(captchaResult);
        return ResponseEntity.ok().build();
    }
}
