package org.eddy.util;

import org.apache.commons.lang3.StringUtils;
import org.eddy.solve.Station;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Justice-love on 2017/7/25.
 */
public class StationNameUtil {

    private static Map<String, List<Station>> stationMaps = new HashMap<>();

    public static void parse(String js) {
        if (StringUtils.isBlank(js)) {
            return;
        }

        String str = StringUtils.substring(js, js.indexOf("'") + 1, js.lastIndexOf("'"));
        String[] arr = str.split("@");
        Map<String, List<Station>> result = Arrays.stream(arr).filter(s -> StringUtils.isNotBlank(s)).map(s -> {
            String[] stations = s.split("\\|");
            return new Station(stations[0], stations[1], stations[2], stations[3]);
        }).collect(Collectors.groupingBy(station -> station.getSimpleCode()));

        stationMaps = result;
    }

}
