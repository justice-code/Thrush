package org.eddy.pipeline;

import org.eddy.pipeline.command.RetryCommand;

public class RetryRunnable implements Runnable {

    @Override
    public void run() {

        while (true) {
            RetryCommand retryCommand = ExceptionPipeline.pollNotify(NotifyRunnable.DEFAULT_TIME);
            if (! retryCommand.needStop()) {
                Pipeline.putNotify(retryCommand);
            }
        }
    }
}
