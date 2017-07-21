package org.eddy.pipeline.command;

import org.eddy.captcha.CaptchaUtil;
import org.eddy.http.HttpRequest;
import org.eddy.im.DingMsgSender;
import org.eddy.im.MarkDownUtil;
import org.eddy.pipeline.CoordinateUtil;

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
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("登录验证图片", url, pipeline));
        }
    },
    LOGIN {
        @Override
        public void execute(String pipeline, Object param) {
            HttpRequest.checkRandCode(CoordinateUtil.computeCoordinate((Integer[]) param));
            String result = HttpRequest.login(CoordinateUtil.computeCoordinate((Integer[]) param));
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result, pipeline));
        }
    },
    REFRESH_LOGIN_CAPTCHA {
        @Override
        public void execute(String pipeline, Object param) {
            String url = CaptchaUtil.saveImage((String) param, HttpRequest.refreshLoginCaptchaImage());
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("登录验证图片", url, pipeline));
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
}
