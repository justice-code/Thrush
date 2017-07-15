package org.eddy.http;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.eddy.config.UrlConfig;
import org.eddy.manager.CookieManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Justice-love on 2017/7/5.
 */
@Component
public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";

    private static PoolingHttpClientConnectionManager pools;

    @Autowired
    private UrlConfig urlConfig;

    public void init() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(urlConfig.getInitUrl());
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("init error", e);
        }
    }

    public void auth() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(urlConfig.getAuth());
        httpPost.setEntity(new StringEntity("appid=otn", ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("auth error", e);
        }
    }

    public byte[] loginCaptchaImage() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(urlConfig.getLoginCaptcha());
        byte[] result = new byte[0];
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            result = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            logger.error("loginCaptchaImage error", e);
        }
        return result;
    }

    public void login(String randCode) {
        CloseableHttpClient httpClient = buildHttpClient();
    }



    //******************************** 私有方法 ****************************************
    private CloseableHttpClient buildHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pools).setUserAgent(USER_AGENT).build();
        return httpClient;
    }

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
