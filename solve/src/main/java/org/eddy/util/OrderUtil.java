package org.eddy.util;

import org.apache.ibatis.ognl.OgnlException;
import org.eddy.http.HttpRequest;

public class OrderUtil {

    public static int needQuery(String json) throws OgnlException {
        int time = Integer.parseInt(JsonUtil.extract("data.waitTime", json));

        if (time < 0) {
            return -1;
        } else {
            return time;
        }
    }

    public static String orderId(String json) throws OgnlException {
        String order = JsonUtil.extract("data.orderId", json);
        return order;
    }

    public static String findOrder(String json) {
        try {
            int time = OrderUtil.needQuery(json);

            if (time > 0) {
                Thread.sleep(time * 1000);
                String result = HttpRequest.queryOrderWaitTime();
                return findOrder(result);
            } else {
                return orderId(json);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
