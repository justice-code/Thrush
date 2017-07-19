package org.eddy.pipeline;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.eddy.captcha.CaptchaUtil;
import org.eddy.http.HttpRequest;
import org.eddy.solve.CaptchaNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/17.
 */
@Getter @Setter @NoArgsConstructor
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotifyRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NotifyRunnable.class);
    // 3分钟
    private static final int DEFAULT_TIME = 3;

    private String pipelineGroup;

    @Autowired
    private LoginPipeline loginPipeline;

    @Autowired
    private HttpRequest httpRequest;

    @Autowired
    private CaptchaUtil captchaUtil;

    @Override
    public void run() {
        httpRequest.init();
        httpRequest.auth();
        String url = captchaUtil.saveImage(imageFileName(), httpRequest.loginCaptchaImage());
        CaptchaNotify loginNotify = findLoginNotify();
        httpRequest.checkRandCode(StringUtils.join(loginNotify.getNumbers(), ","));

    }

    //***************************************************** private *******************************************************

    private String imageFileName() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.toLocalDate().toString() + "/" + dateTime.toLocalTime().withNano(0).format(DateTimeFormatter.ISO_LOCAL_TIME) + ".jpg";
    }

    private CaptchaNotify findLoginNotify() {
        CaptchaNotify notify = loginPipeline.pollNotify(DEFAULT_TIME);
        Objects.requireNonNull(notify);

        if (StringUtils.equals(notify.getPipeline(), this.getPipelineGroup())) {
            return notify;
        }

        try {
            loginPipeline.putNotify(notify);
            Thread.sleep(300);
            return findLoginNotify();
        } catch (InterruptedException e) {
            throw new RuntimeException("findNotify error", e);
        }
    }
}
