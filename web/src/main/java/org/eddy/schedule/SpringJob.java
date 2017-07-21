package org.eddy.schedule;

import org.eddy.im.DingMsgSender;
import org.eddy.im.MarkDownUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/21.
 */
@Component
@EnableScheduling
public class SpringJob {

    @Scheduled(cron = "10 1 9 * * * ")
    public void begin() {
        DingMsgSender.markdown.sendMsg(MarkDownUtil.begin());
    }
}
