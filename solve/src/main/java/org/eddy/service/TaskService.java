package org.eddy.service;

import org.eddy.pipeline.NotifyRunnable;
import org.eddy.pipeline.Pipeline;
import org.eddy.pipeline.command.Command;
import org.eddy.pipeline.command.CommandNotify;
import org.eddy.solve.NotifyThreadFactory;
import org.springframework.stereotype.Service;

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
        String notifyGroup = genNotifyGroup();

        NotifyRunnable runnable = new NotifyRunnable();
        runnable.setPipelineGroup(notifyGroup);
        pool.submit(runnable);

        CommandNotify notify = new CommandNotify();
        notify.setPipeline(notifyGroup);
        notify.setArg(imageFileName());
        notify.setCommand(Command.INIT);
        Pipeline.putNotify(notify);

        return notifyGroup;
    }

    private String genNotifyGroup() {
        return "task-" + aLong.getAndIncrement();
    }

    private String imageFileName() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.toLocalDate().toString() + "/" + dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH-mm-ss-SSS")) + ".jpg";
    }
}
