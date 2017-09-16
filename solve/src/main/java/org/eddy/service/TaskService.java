package org.eddy.service;

import org.eddy.captcha.CaptchaUtil;
import org.eddy.pipeline.ExceptionPipeline;
import org.eddy.pipeline.NotifyRunnable;
import org.eddy.pipeline.Pipeline;
import org.eddy.pipeline.RetryRunnable;
import org.eddy.pipeline.command.Command;
import org.eddy.pipeline.command.CommandNotify;
import org.eddy.solve.NotifyThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Justice-love on 2017/7/18.
 */
@Service
public class TaskService {

    private static final ExecutorService pool = new ThreadPoolExecutor(20, 100, 3L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3_000), new NotifyThreadFactory());

    private AtomicLong aLong = new AtomicLong(1);

    public String submit() {
        String pipeline = genPipeline();

        NotifyRunnable runnable = new NotifyRunnable();
        runnable.setPipelineGroup(pipeline);
        pool.submit(runnable);

        CommandNotify notify = new CommandNotify();
        notify.setPipeline(pipeline);
        notify.setArg(CaptchaUtil.imageFileName());
        notify.setCommand(Command.INIT);
        Pipeline.putNotify(notify);

        return pipeline;
    }

    @PostConstruct
    public void init() {
        pool.submit(new RetryRunnable());
    }

    private String genPipeline() {
        return "task-" + aLong.getAndIncrement();
    }

}
