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
        public Object execute(HttpRequest httpRequest, Object param) {
            httpRequest.init();
            httpRequest.auth();
            return CaptchaUtil.saveImage((String) param, httpRequest.loginCaptchaImage());
        }
    },
    LOGIN {
        @Override
        public Object execute(HttpRequest httpRequest, Object param) {
            httpRequest.checkRandCode(CoordinateUtil.computeCoordinate((Integer[]) param));
            httpRequest.login(CoordinateUtil.computeCoordinate((Integer[]) param));
            return null;
        }
    },
    REFRESH_LOGIN_CAPTCHA {
        @Override
        public Object execute(HttpRequest httpRequest, Object param) {
            return CaptchaUtil.saveImage((String) param, httpRequest.refreshLoginCaptchaImage());
        }
    },
    STOP {
        @Override
        public Object execute(HttpRequest httpRequest, Object param) {
            return super.execute(httpRequest, param);
        }
    };

    public Object execute(HttpRequest httpRequest, Object param) {
        throw new RuntimeException("unsupported");
    }
}
