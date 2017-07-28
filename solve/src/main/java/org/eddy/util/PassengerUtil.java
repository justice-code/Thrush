package org.eddy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.eddy.AssembleUtil;
import org.eddy.solve.Passenger;
import org.eddy.web.TrainQuery;

import java.beans.IntrospectionException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Justice-love on 2017/7/26.
 */
public class PassengerUtil {

    public static List<String> parsePassengerName(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("normal_passengers");
        return jsonArray.stream().map(j -> {
            JSONObject jo = (JSONObject) j;
            return jo.getString("passenger_name");
        }).collect(Collectors.toList());
    }

    public static List<Passenger> parsePassenger(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("normal_passengers");
        return jsonArray.stream().map(j -> {
            JSONObject param = (JSONObject) j;
            try {
                return AssembleUtil.assemble(Passenger.class, param);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public static Passenger getPassenger(List<Passenger> passengers, TrainQuery query) {
        Objects.requireNonNull(passengers);
        Objects.requireNonNull(query);

        return passengers.stream().filter(passenger -> StringUtils.equals(query.getPassenger(), passenger.getName())).findFirst().orElse(new Passenger());
    }
}
