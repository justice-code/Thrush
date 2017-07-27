package org.eddy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.eddy.AssembleUtil;
import org.eddy.solve.Ticket;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Justice-love on 2017/7/27.
 */
public class TicketUtil {

    public static List<Ticket> genTickets(String json) {
        Objects.requireNonNull(json);

        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("result");
        return jsonArray.stream().map(s -> {
            String str = (String) s;
            String[] arr = str.split("\\|");
            try {
                return AssembleUtil.assemble(Ticket.class, arr);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
