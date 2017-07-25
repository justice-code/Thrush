package org.eddy.listener;

import org.eddy.config.DingConfig;
import org.eddy.http.HttpRequest;
import org.eddy.im.DingMsgSender;
import org.eddy.im.MarkDownUtil;
import org.eddy.util.StationNameUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by Justice-love on 2017/7/24.
 */
public class ThrushApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        DingMsgSender.markdown.sendMsg(MarkDownUtil.begin(), DingConfig.notifyToken);
        StationNameUtil.parse(HttpRequest.stationName());
    }
}
