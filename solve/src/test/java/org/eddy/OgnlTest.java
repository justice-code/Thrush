package org.eddy;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.eddy.manager.ResultManager;
import org.eddy.solve.ResultKey;
import org.eddy.util.JsonUtil;
import org.junit.Test;

/**
 * Created by Justice-love on 2017/7/24.
 */
public class OgnlTest {

    @Test
    public void test() throws OgnlException {
        JSONObject jsonObject = JSONObject.parseObject("{\"result_message\":\"验证通过\",\"result_code\":0,\"apptk\":null,\"newapptk\":\"3NXQt63mvUCRihvGv1gn9pM2WFtDXd0e0V5Wyt2_4NYSflEqrwe1e0\"}");
        Object value = Ognl.getValue("newapptk", jsonObject);
        System.out.println(value);
    }

    @Test
    public void test2() {
        String result = "{\"result_message\":\"验证通过\",\"result_code\":0,\"apptk\":null,\"newapptk\":\"B3IYGpzR2f271MTe-3pJ8Qr1OeKwRyjda8PuSKS2VZkygGQE73e1e0\"}\n";
        ResultManager.touch(result, new ResultKey("tk", "newapptk"));
    }

    @Test
    public void test3() throws OgnlException {
        String json = "{\"validateMessagesShowId\":\"_validatorMessage\",\"status\":true,\"httpstatus\":200,\"data\":{\"queryOrderWaitTimeStatus\":true,\"count\":0,\"waitTime\":-1,\"requestId\":6299411751752095828,\"waitCount\":0,\"tourFlag\":\"dc\",\"orderId\":\"E165509351\"},\"messages\":[],\"validateMessages\":{}}";
        System.out.println(JsonUtil.extract("data.waitTime", json));
    }
}
