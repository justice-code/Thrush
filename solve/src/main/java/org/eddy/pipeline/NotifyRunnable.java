package org.eddy.pipeline;

import lombok.*;
import org.eddy.solve.CaptchaNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Justice-love on 2017/7/17.
 */
@Getter @Setter @RequiredArgsConstructor @NoArgsConstructor
public class NotifyRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NotifyRunnable.class);
    // 3分钟
    private static final int DEFAULT_TIME = 3;

    @NonNull
    private String notifyGroup;

    @Autowired
    private Pipeline pipeline;

    @Override
    public void run() {
        CaptchaNotify notify = pipeline.pollNotify(DEFAULT_TIME);
    }
}
