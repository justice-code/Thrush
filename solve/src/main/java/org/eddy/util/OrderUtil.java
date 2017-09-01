package org.eddy.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.OgnlException;
import org.eddy.http.HttpRequest;

import java.util.concurrent.TimeUnit;

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
        if (StringUtils.isBlank(order)) {
            return JsonUtil.extract("data.msg", json);
        } else {
            return order;
        }
    }

    public static String findOrder(String json) {
        try {
            int time = OrderUtil.needQuery(json);

            if (time > 0) {
                TimeUnit.SECONDS.sleep(2);
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
