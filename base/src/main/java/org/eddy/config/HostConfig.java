package org.eddy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/21.
 */
@ConfigurationProperties("thrush.host")
@Component
public class HostConfig {

    public static String domain;

    public void setDomain(String domain) {
        HostConfig.domain = domain;
    }
}
