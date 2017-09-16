package org.eddy.pipeline;

import org.eddy.config.DingConfig;
import org.eddy.im.DingMsgSender;
import org.eddy.im.MarkDownUtil;
import org.eddy.pipeline.command.RetryCommand;

public class RetryRunnable implements Runnable {

    @Override
    public void run() {

        while (true) {
            RetryCommand retryCommand = ExceptionPipeline.pollNotify(NotifyRunnable.DEFAULT_TIME);
            if (! retryCommand.needStop()) {
                Pipeline.putNotify(retryCommand);
            } else {
                DingMsgSender.markdown.sendMsg(retryCommand.getException().getMessage(), DingConfig.token);
            }
        }
    }
}
