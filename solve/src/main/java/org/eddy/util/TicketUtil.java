package org.eddy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.eddy.AssembleUtil;
import org.eddy.solve.Ticket;
import org.eddy.web.TrainQuery;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.*;
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

    public static Ticket findTicket(List<Ticket> tickets, TrainQuery query) {
        Objects.requireNonNull(query);
        query.getALong().getAndIncrement();

        return Optional.ofNullable(tickets).orElse(new ArrayList<>()).stream().filter(ticket -> {
            return StringUtils.equals("Y", ticket.getCanBuy()) && StringUtils.equalsIgnoreCase(ticket.getTrainNo(), query.getTrain()) && seatCheck(ticket, query);
        }).findFirst().orElse(null);
    }

    /**
     * 座次校验
     * @param ticket
     * @param query
     * @return true: 对应座次有票
     */
    private static boolean seatCheck(Ticket ticket, TrainQuery query) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(ticket.getClass());
            return Arrays.stream(beanInfo.getPropertyDescriptors()).filter(p -> StringUtils.equals(p.getName(), query.getSeat())).findFirst().map(p -> {
                try {
                    Object fieldValue = p.getReadMethod().invoke(ticket);
                    return null != fieldValue && ! "".equals(fieldValue.toString()) && ! StringUtils.equals("无", fieldValue.toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).orElse(false);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}
