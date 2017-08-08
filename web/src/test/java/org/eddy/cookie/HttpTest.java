package org.eddy.cookie;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.eddy.config.UrlConfig;
import org.eddy.http.HttpRequest;
import org.eddy.manager.CookieManager;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Justice-love on 2017/8/8.
 */
public class HttpTest {

    @Test
    public void test() throws IOException {
        CloseableHttpClient httpClient = HttpRequest.buildHttpClient();

        HttpGet httpGet = new HttpGet("https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?random=1502156832959&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=e99982b32d62670cd76b55768f9a6194");
        httpGet.addHeader("Cookie", "JSESSIONID=B2F7EFED9C4665844785D4AE92CF704A; tk=lrJjUOa8XlvsNwLoNXhoOgY_C-tQ8FikjbA188-5IGsVE30L27e1e0; route=c5c62a339e7744272a54643b3be5bf64; BIGipServerotn=2196242698.64545.0000; BIGipServerpassport=854065418.50215.0000");

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }
    }
}
