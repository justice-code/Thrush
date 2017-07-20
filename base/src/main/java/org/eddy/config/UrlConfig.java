package org.eddy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/5.
 */
@Getter @Setter
@ConfigurationProperties("thrush.url")
@Component
public class UrlConfig {

    public static String initUrl;

    public static String loginCaptcha;

    public static String refreshLoginCaptcha;

    public static String auth;

    public static String checkCode;

    public static String loginUrl;
}
