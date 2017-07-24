package org.eddy;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.eddy.manager.ResultManager;
import org.eddy.solve.ResultKey;
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
}
