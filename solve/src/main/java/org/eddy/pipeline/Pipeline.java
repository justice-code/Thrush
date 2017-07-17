package org.eddy.pipeline;

import org.eddy.solve.CaptchaNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Justice-love on 2017/7/16.
 */
public class Pipeline {

    private static final Logger logger = LoggerFactory.getLogger(Pipeline.class);

    private BlockingQueue<CaptchaNotify> queue = new LinkedBlockingQueue<>(3_000);

    public void putNotify(CaptchaNotify notify) {
        Objects.requireNonNull(notify);
        try {
            queue.put(notify);
        } catch (InterruptedException e) {
            throw new RuntimeException("put CaptchaNotify error");
        }
    }

    public CaptchaNotify takeNotify() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("take CaptchaNotify error");
        }
    }

}
