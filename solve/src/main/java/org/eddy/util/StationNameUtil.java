package org.eddy.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eddy.solve.Station;

import java.util.*;
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

    public static Station findStation(String simpleCode) {
        List<Station> stations = stationMaps.get(simpleCode);
        return Optional.ofNullable(stations).filter(s -> CollectionUtils.isNotEmpty(s)).map(s -> s.get(0)).orElse(null);
    }

}
