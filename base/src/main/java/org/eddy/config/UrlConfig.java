package org.eddy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/5.
 */
@ConfigurationProperties("thrush.url")
@Component
public class UrlConfig {

    public static String initUrl;

    public static String loginCaptcha;

    public static String refreshLoginCaptcha;

    public static String auth;

    public static String checkCode;

    public static String loginUrl;

    public static String uamtk;

    public void setUamtk(String uamtk) {
        UrlConfig.uamtk = uamtk;
    }

    public void setInitUrl(String initUrl) {
        UrlConfig.initUrl = initUrl;
    }

    public void setLoginCaptcha(String loginCaptcha) {
        UrlConfig.loginCaptcha = loginCaptcha;
    }

    public void setRefreshLoginCaptcha(String refreshLoginCaptcha) {
        UrlConfig.refreshLoginCaptcha = refreshLoginCaptcha;
    }

    public void setAuth(String auth) {
        UrlConfig.auth = auth;
    }

    public void setCheckCode(String checkCode) {
        UrlConfig.checkCode = checkCode;
    }

    public void setLoginUrl(String loginUrl) {
        UrlConfig.loginUrl = loginUrl;
    }
}
