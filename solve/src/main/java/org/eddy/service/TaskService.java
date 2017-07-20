package org.eddy.service;

import org.eddy.pipeline.NotifyRunnable;
import org.eddy.pipeline.Pipeline;
import org.eddy.pipeline.command.Command;
import org.eddy.pipeline.command.CommandNotify;
import org.eddy.solve.NotifyThreadFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class TaskService implements ApplicationContextAware{

    private static final ExecutorService pool = new ThreadPoolExecutor(20, 100, 3L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3_000), new NotifyThreadFactory());

    private AtomicLong aLong = new AtomicLong(1);

    private ApplicationContext applicationContext;

    public String submit() {
        String notifyGroup = genNotifyGroup();
        NotifyRunnable runnable = applicationContext.getBean(NotifyRunnable.class);
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private String imageFileName() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.toLocalDate().toString() + "/" + dateTime.toLocalTime().withNano(0).format(DateTimeFormatter.ISO_LOCAL_TIME) + ".jpg";
    }
}
