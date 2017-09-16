package org.eddy.pipeline;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.eddy.pipeline.command.CommandNotify;
import org.eddy.pipeline.command.RetryCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/17.
 */
@Getter @Setter @NoArgsConstructor
public class NotifyRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NotifyRunnable.class);
    // 3分钟
    private static final int DEFAULT_TIME = 10;

    private static final int RETRY_TIME = 3;

    private String pipelineGroup;

    @Override
    public void run() {
        execute();
    }

    //***************************************************** private *******************************************************

    private void execute() {
        CommandNotify commandNotify = null;
        try {
            commandNotify = findLoginNotify();
            commandNotify.getCommand().execute(this.getPipelineGroup(), commandNotify.getArg());
            execute();
        } catch (Exception e) {
            RetryCommand retryCommand;

            if (null == commandNotify) {
                retryCommand = new RetryCommand(null);
                retryCommand.setRetryTime(Integer.MAX_VALUE - 1);
            } else if (commandNotify instanceof RetryCommand) {
                retryCommand = (RetryCommand) commandNotify;
            } else {
                retryCommand = new RetryCommand(commandNotify);
            }
            retryCommand.increase();

            ExceptionPipeline.putNotify(retryCommand);
            execute();
        }
    }

    private CommandNotify findLoginNotify() {
        CommandNotify notify = Pipeline.pollNotify(DEFAULT_TIME);
        Objects.requireNonNull(notify);

        if (StringUtils.equals(notify.getPipeline(), this.getPipelineGroup())) {
            return notify;
        }

        try {
            Pipeline.putNotify(notify);
            Thread.sleep(300);
            return findLoginNotify();
        } catch (InterruptedException e) {
            throw new RuntimeException("findNotify error", e);
        }
    }
}
