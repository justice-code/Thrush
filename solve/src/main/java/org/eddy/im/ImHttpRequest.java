package org.eddy.im;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.eddy.config.DingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Justice-love on 2017/7/21.
 */
public class ImHttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(ImHttpRequest.class);

    private static PoolingHttpClientConnectionManager pools;

    public static boolean send(DingMsgSender sender, String content, String token) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pools).build();
        HttpPost post = new HttpPost(DingConfig.url + token);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(buildJsonMessage(sender, content), "UTF-8"));
        try(CloseableHttpResponse httpResponse = httpClient.execute(post)) {
            return convertResponse(httpResponse.getEntity());
        } catch (IOException e) {
            logger.error("send error", e);
            return false;
        }
    }

    //********************************************* 私有方法 **********************************

    private static boolean convertResponse(HttpEntity response) throws IOException {
        String strResponse = EntityUtils.toString(response);
        JSONObject jsonResponse = JSONObject.parseObject(strResponse);
        return jsonResponse.containsKey("errcode") && jsonResponse.getIntValue("errcode") == 0;
    }

    private static String buildJsonMessage(DingMsgSender sender, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", sender.name());
        jsonObject.put(sender.name(), content);
        JSONObject at = new JSONObject();
        at.put("isAtAll", true);
        jsonObject.put("at", at);
        return jsonObject.toJSONString();
    }

    //********************************************* 代码块 **********************************
    static {
        if (pools == null) {
            pools = new PoolingHttpClientConnectionManager();
            pools.setMaxTotal(50);
            pools.setDefaultMaxPerRoute(5);
        }
    }
}
