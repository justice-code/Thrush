package org.eddy.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
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
import org.eddy.config.UserConfig;
import org.eddy.manager.CookieManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/5.
 */
public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";

    private static PoolingHttpClientConnectionManager pools;

    public static void init() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(UrlConfig.initUrl);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("init error", e);
        }
    }

    public static void auth() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.auth);

        httpPost.addHeader(CookieManager.cookieHeader());
        httpPost.setEntity(new StringEntity("appid=otn", ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("auth error", e);
        }
    }

    public byte[] loginCaptchaImage() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(UrlConfig.loginCaptcha);

        httpGet.addHeader(CookieManager.cookieHeader());

        byte[] result = new byte[0];
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
            result = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            logger.error("loginCaptchaImage error", e);
        }

        return result;
    }

    public byte[] refreshLoginCaptchaImage() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(UrlConfig.refreshLoginCaptcha);

        httpGet.addHeader(CookieManager.cookieHeader());

        byte[] result = new byte[0];
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
            result = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            logger.error("refreshLoginCaptchaImage error", e);
        }

        return result;
    }

    public static void checkRandCode(String randCode) {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.checkCode);

        httpPost.addHeader(CookieManager.cookieHeader());
        httpPost.setEntity(new StringEntity("answer=" + encode(randCode) + "&login_site=E&&rand=sjrand", ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
            logger.debug(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            logger.error("checkRandCode error", e);
        }
    }

    public static void login(String randCode) {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.loginUrl);

        httpPost.addHeader(CookieManager.cookieHeader());
        String param = "username=" + encode(UserConfig.username) + "&password=" + encode(UserConfig.password) + "&appid=otn";
        httpPost.setEntity(new StringEntity(param, ContentType.APPLICATION_FORM_URLENCODED));

        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("login error", e);
        }

    }



    //******************************** 私有方法 ****************************************
    private static String encode(String param) {
        Objects.requireNonNull(param);
        String result = StringUtils.EMPTY;

        try {
            result = URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("encode charset error", e);
        }

        return result;
    }

    private static CloseableHttpClient buildHttpClient() {
        return HttpClients.custom().setConnectionManager(pools).setUserAgent(USER_AGENT).build();
    }

    private static SSLContext createContext() {
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
    static {
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
