package org.eddy.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Justice-love on 2017/7/28.
 */
@ConfigurationProperties("thrush.seat")
@Component
public class SeatConfig {

    public static String[] type;

    public static void setType(String[] type) {
        SeatConfig.type = type;
    }

    public static String getSeatType(String seat) {
        for (String st : type) {
            String[] arr = st.split(":");
            if (StringUtils.equals(arr[0], seat)) {
                return arr[1];
            }
        }
        return StringUtils.EMPTY;
    }
}
