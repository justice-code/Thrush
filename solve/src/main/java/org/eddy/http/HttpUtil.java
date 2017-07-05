package org.eddy.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eddy.config.UrlConfig;

import java.io.IOException;

/**
 * Created by Justice-love on 2017/7/5.
 */
public class HttpUtil {

    private static PoolingHttpClientConnectionManager pools;

    private UrlConfig urlConfig = new UrlConfig();

    {
        if (pools == null) {
            pools = new PoolingHttpClientConnectionManager();
            pools.setMaxTotal(50);
            pools.setDefaultMaxPerRoute(5);
        }
    }

    public void init() throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pools).build();
        HttpGet httpGet = new HttpGet(urlConfig.getInitUrl());
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            System.out.println(response);
        }
    }


}
