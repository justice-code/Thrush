package org.eddy.http;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.eddy.config.UrlConfig;
import org.eddy.manager.CookieManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * Created by Justice-love on 2017/7/5.
 */
@Component
public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static PoolingHttpClientConnectionManager pools;

    @Autowired
    private UrlConfig urlConfig;

    public void init() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pools).build();
        HttpGet httpGet = new HttpGet(urlConfig.getInitUrl());
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("init error", e);
        }
    }

    public void auth() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pools).build();
        HttpPost httpPost = new HttpPost(urlConfig.getAuth());
        httpPost.setEntity(new StringEntity("appid=otn", ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("auth error", e);
        }
    }

    public byte[] loginCaptchaImage() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pools).build();
        HttpGet httpGet = new HttpGet(urlConfig.getLoginCaptcha());
        byte[] result = new byte[0];
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            result = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            logger.error("loginCaptchaImage error", e);
        }
        return result;
    }

    //******************************** 私有方法 ****************************************
    private SSLContext createContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            context.init(null, new TrustManager[] {tm},null);
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    //******************************** 代码块 ****************************************
    {
        if (pools == null) {
            SSLConnectionSocketFactory mineSsl = new SSLConnectionSocketFactory(createContext(), NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", mineSsl)
                    .build();
            pools = new PoolingHttpClientConnectionManager(registry);
            pools.setMaxTotal(50);
            pools.setDefaultMaxPerRoute(5);
        }
    }


}
