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

    public static String[] seatTypes;

    public void setSeatTypes(String[] seatTypes) {
        SeatConfig.seatTypes = seatTypes;
    }

    public static String getSeatType(String seat) {
        for (String st : seatTypes) {
            String[] arr = st.split(":");
            if (StringUtils.equals(arr[0], seat)) {
                return arr[1];
            }
        }
        return StringUtils.EMPTY;
    }
}
