package org.eddy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/21.
 */
@ConfigurationProperties("thrush.ding")
@Component
public class DingConfig {

    public static String url;

    public static String token;

    public static String notifyToken;

    public void setUrl(String url) {
        DingConfig.url = url;
    }

    public void setToken(String token) {
        DingConfig.token = token;
    }

    public void setNotifyToken(String notifyToken) {
        DingConfig.notifyToken = notifyToken;
    }
}
