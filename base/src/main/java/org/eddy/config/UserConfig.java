package org.eddy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/15.
 */
@Getter @Setter
@Component
@ConfigurationProperties("thrush.user")
public class UserConfig {

    private String username;

    private String password;
}
