package org.eddy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/15.
 */
@Component
@ConfigurationProperties("thrush.user")
public class UserConfig {

    public static String username;

    public static String password;

    public void setUsername(String username) {
        UserConfig.username = username;
    }

    public void setPassword(String password) {
        UserConfig.password = password;
    }
}
