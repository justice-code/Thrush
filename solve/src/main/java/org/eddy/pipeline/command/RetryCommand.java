package org.eddy.pipeline.command;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class RetryCommand extends CommandNotify {

    private int retryTime;

    private Exception exception;

    private static final int RETRY_TIME = 3;

    public RetryCommand(CommandNotify commandNotify) {
        Objects.requireNonNull(commandNotify);
        super.command = commandNotify.getCommand();
        super.arg = commandNotify.getArg();
        super.pipeline = commandNotify.getPipeline();
    }

    public void increase() {
        retryTime++;
    }

    public boolean needStop() {
        return retryTime >= RETRY_TIME;
    }

    public void complete(Exception e) {
        this.exception = e;
    }
}
