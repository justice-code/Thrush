package org.eddy.pipeline.command;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class RetryCommand extends CommandNotify {

    private int retryTime;

    public RetryCommand(CommandNotify commandNotify, int retryTime) {
        Objects.requireNonNull(commandNotify);
        this.retryTime = retryTime;
        super.command = commandNotify.getCommand();
        super.arg = commandNotify.getArg();
        super.pipeline = commandNotify.getPipeline();
    }
}
