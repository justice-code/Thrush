package org.eddy.pipeline.command;

import org.eddy.captcha.CaptchaUtil;
import org.eddy.config.DingConfig;
import org.eddy.config.HostConfig;
import org.eddy.http.HttpRequest;
import org.eddy.im.DingMsgSender;
import org.eddy.im.MarkDownUtil;
import org.eddy.pipeline.CoordinateUtil;
import org.eddy.util.PassengerUtil;
import org.eddy.web.TrainQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Justice-love on 2017/7/20.
 */
public enum Command {

    INIT {
        @Override
        public void execute(String pipeline, Object param) {
            HttpRequest.init();
            HttpRequest.auth();
            String url = CaptchaUtil.saveImage((String) param, HttpRequest.loginCaptchaImage());
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("登录验证图片", url, pipeline), DingConfig.token);
        }
    },
    LOGIN {
        @Override
        public void execute(String pipeline, Object param) {
            HttpRequest.checkRandCode(CoordinateUtil.computeCoordinate((Integer[]) param));
            String result = HttpRequest.login(CoordinateUtil.computeCoordinate((Integer[]) param));
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result, pipeline), DingConfig.token);
            String uamtkResult = HttpRequest.uamtk();
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(uamtkResult), DingConfig.token);
            String authClientResult = HttpRequest.authClient();
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(authClientResult, "获取火车票填写页面", HostConfig.domain + "/web/tranBegin", pipeline), DingConfig.token);
        }
    },
    REFRESH_LOGIN_CAPTCHA {
        @Override
        public void execute(String pipeline, Object param) {
            String url = CaptchaUtil.saveImage((String) param, HttpRequest.refreshLoginCaptchaImage());
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("登录验证图片", url, pipeline), DingConfig.token);
        }
    },
    START_CHOOSE_TRAIN {
        @Override
        public void execute(String pipeline, Object param) {
            String passengers = String.join(",", PassengerUtil.parsePassengerName(HttpRequest.getPassengers()));
//            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(passengers), DingConfig.token);

            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(passengers, "火车票填写", HostConfig.domain + "/web/train?passengers=" + passengers, pipeline), DingConfig.token);
        }
    },
    TICKET_QUERY {
        @Override
        public void execute(String pipeline, Object param) {
            String ticket = HttpRequest.ticketQuery((TrainQuery) param);
            logger.debug(ticket);
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(ticket), DingConfig.token);
        }
    },
    STOP {
        @Override
        public void execute(String pipeline, Object param) {
            super.execute(pipeline, param);
        }
    };

    public void execute(String pipeline, Object param) {
        throw new RuntimeException("unsupported");
    }

    private static final Logger logger = LoggerFactory.getLogger(Command.class);
}
