package org.eddy.pipeline;

import org.eddy.pipeline.command.RetryCommand;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ExceptionPipeline {

    private static BlockingQueue<RetryCommand> queue = new LinkedBlockingQueue<>(3_000);

    public static void putNotify(RetryCommand notify) {
        Objects.requireNonNull(notify);
        try {
            queue.put(notify);
        } catch (InterruptedException e) {
            throw new RuntimeException("put CaptchaNotify error", e);
        }
    }

    public static RetryCommand pollNotify(int minutes) {
        try {
            return queue.poll(minutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException("take CaptchaNotify error", e);
        }
    }

}
