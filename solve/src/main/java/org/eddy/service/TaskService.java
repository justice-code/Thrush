package org.eddy.service;

import org.eddy.pipeline.NotifyRunnable;
import org.eddy.solve.NotifyThreadFactory;
import org.springframework.stereotype.Service;

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

    private static final ExecutorService pool = new ThreadPoolExecutor(
            20,
            100,
            3L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(3_000),
            new NotifyThreadFactory());

    private AtomicLong aLong = new AtomicLong(1);

    public String submit() {
        String notifyGroup = genNotifyGroup();
        pool.submit(new NotifyRunnable(notifyGroup));
        return notifyGroup;
    }

    private String genNotifyGroup() {
        return "task-" + aLong.getAndIncrement();
    }
}
