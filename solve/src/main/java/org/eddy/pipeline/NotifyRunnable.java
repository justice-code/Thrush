package org.eddy.pipeline;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.eddy.solve.CaptchaNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/17.
 */
@Getter @Setter @RequiredArgsConstructor @NoArgsConstructor
public class NotifyRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NotifyRunnable.class);
    // 3分钟
    private static final int DEFAULT_TIME = 3;

    @NonNull
    private String pipelineGroup;

    @Autowired
    private Pipeline pipeline;

    @Override
    public void run() {
        CaptchaNotify notify = pipeline.pollNotify(DEFAULT_TIME);
        Objects.requireNonNull(notify);
        if (! StringUtils.equals(notify.getPipeline(), this.getPipelineGroup())) {

        }
    }

    private CaptchaNotify findNotify() {
        CaptchaNotify notify = pipeline.pollNotify(DEFAULT_TIME);
        Objects.requireNonNull(notify);

        if (StringUtils.equals(notify.getPipeline(), this.getPipelineGroup())) {
            return notify;
        }

        try {
            pipeline.putNotify(notify);
            Thread.sleep(300);
            return findNotify();
        } catch (InterruptedException e) {
            throw new RuntimeException("findNotify error", e);
        }
    }
}
