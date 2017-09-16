package org.eddy.pipeline;

import org.eddy.pipeline.command.CommandNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Justice-love on 2017/7/16.
 */
public class Pipeline {

    private static final Logger logger = LoggerFactory.getLogger(Pipeline.class);

    private static BlockingQueue<CommandNotify> queue = new LinkedBlockingQueue<>(3_000);

    public static void putNotify(CommandNotify notify) {
        Objects.requireNonNull(notify);
        try {
            queue.put(notify);
        } catch (InterruptedException e) {
            throw new RuntimeException("put CaptchaNotify error", e);
        }
    }

    public static CommandNotify pollNotify(int minutes) {
        try {
            return queue.poll(minutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException("take CaptchaNotify error", e);
        }
    }

}
