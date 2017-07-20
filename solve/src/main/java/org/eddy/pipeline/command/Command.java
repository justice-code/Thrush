package org.eddy.pipeline.command;

import org.eddy.captcha.CaptchaUtil;
import org.eddy.http.HttpRequest;
import org.eddy.pipeline.CoordinateUtil;

/**
 * Created by Justice-love on 2017/7/20.
 */
public enum Command {

    INIT {
        @Override
        public Object execute(Object param) {
            HttpRequest.init();
            HttpRequest.auth();
            return CaptchaUtil.saveImage((String) param, HttpRequest.loginCaptchaImage());
        }
    },
    LOGIN {
        @Override
        public Object execute(Object param) {
            HttpRequest.checkRandCode(CoordinateUtil.computeCoordinate((Integer[]) param));
            HttpRequest.login(CoordinateUtil.computeCoordinate((Integer[]) param));
            return null;
        }
    },
    REFRESH_LOGIN_CAPTCHA {
        @Override
        public Object execute(Object param) {
            return CaptchaUtil.saveImage((String) param, HttpRequest.refreshLoginCaptchaImage());
        }
    },
    STOP {
        @Override
        public Object execute(Object param) {
            return super.execute(param);
        }
    };

    public Object execute(Object param) {
        throw new RuntimeException("unsupported");
    }
}
