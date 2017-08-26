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
import org.eddy.config.SeatConfig;
import org.eddy.config.UrlConfig;
import org.eddy.config.UserConfig;
import org.eddy.manager.CookieManager;
import org.eddy.manager.ResultManager;
import org.eddy.solve.Passenger;
import org.eddy.solve.ResultKey;
import org.eddy.solve.Ticket;
import org.eddy.util.PassengerUtil;
import org.eddy.util.StationNameUtil;
import org.eddy.util.TokenUtil;
import org.eddy.web.TrainQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public static byte[] loginCaptchaImage() {
        return captchaImage(UrlConfig.loginCaptcha);
    }

    public static byte[] refreshLoginCaptchaImage() {
        return captchaImage(UrlConfig.refreshLoginCaptcha);
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

    public static String login(String randCode) {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.loginUrl);

        httpPost.addHeader(CookieManager.cookieHeader());
        String param = "username=" + encode(UserConfig.username) + "&password=" + encode(UserConfig.password) + "&appid=otn";
        httpPost.setEntity(new StringEntity(param, ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            result = EntityUtils.toString(response.getEntity());
            CookieManager.touch(response);
            ResultManager.touch(result, new ResultKey("uamtk", "uamtk"));
        } catch (IOException e) {
            logger.error("login error", e);
        }

        return result;
    }

    public static String uamtk() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.uamtk);

        httpPost.addHeader(CookieManager.cookieHeader());
        httpPost.setEntity(new StringEntity("appid=otn", ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        String result = StringUtils.EMPTY;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            result = EntityUtils.toString(response.getEntity());
            CookieManager.touch(response);
            ResultManager.touch(result, new ResultKey("tk", "newapptk"));
        } catch (IOException e) {
            logger.error("uamtk error", e);
        }

        return result;
    }

    public static String authClient() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.authClient);

        httpPost.addHeader(CookieManager.cookieHeader());
        String param = Optional.ofNullable(ResultManager.get("tk")).map(r -> null == r.getValue() ? StringUtils.EMPTY : r.getValue().toString()).orElse(StringUtils.EMPTY);
        httpPost.setEntity(new StringEntity("tk=" + param, ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        String result = StringUtils.EMPTY;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            result = EntityUtils.toString(response.getEntity());
            CookieManager.touch(response);
        } catch (IOException e) {
            logger.error("authClient error", e);
        }

        return result;
    }

    public static String stationName() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(UrlConfig.stationName);

        String result = StringUtils.EMPTY;
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("stationName error", e);
        }

        return result;
    }

    public static String getPassengers() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.passenger);

        httpPost.addHeader(CookieManager.cookieHeader());

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            result = EntityUtils.toString(response.getEntity());
            List<Passenger> passengers = PassengerUtil.parsePassenger(result);
            ResultManager.touch(passengers, "passengers");
        } catch (IOException e) {
            logger.error("getPassengers error", e);
        }

        return result;
    }

    public static String ticketQuery(TrainQuery trainQuery) {
        Objects.requireNonNull(trainQuery);

        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(UrlConfig.ticketQuery + "?" + genQueryParam(trainQuery));

        httpGet.setHeader(CookieManager.cookieHeader());

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("ticketQuery error", e);
        }

        return result;
    }

    public static String checkUser() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.checkUser);

        httpPost.addHeader(CookieManager.cookieHeader());

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("checkUser error", e);
        }

        return result;
    }

    public static String submitOrderRequest(Ticket ticket, TrainQuery query) {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.submitOrderRequest);

        httpPost.addHeader(CookieManager.cookieHeader());
        String param = genSubmitOrderRequestParam(ticket, query);
        httpPost.setEntity(new StringEntity(param, ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("checkUser error", e);
        }

        return result;
    }

    //ifShowPassCode
    public static String checkOrderInfo(TrainQuery query) {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.checkOrderInfo);

        httpPost.addHeader(CookieManager.cookieHeader());

        httpPost.setEntity(new StringEntity(genCheckOrderInfoParam(query), ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("checkUser error", e);
        }

        return result;
    }

    public static void initDc() {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.initDc);

        httpPost.addHeader(CookieManager.cookieHeader());

        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
            String result = EntityUtils.toString(response.getEntity());
            String token = TokenUtil.getToken(result);
            ResultManager.touch(token, "repeatSubmitToken");
            String purposeCodes = TokenUtil.getPurposeCodes(result);
            ResultManager.touch(purposeCodes, "purposeCodes");
            String keyCheck = TokenUtil.getKeyCheck(result);
            ResultManager.touch(keyCheck, "keyCheck");
        } catch (IOException e) {
            logger.error("checkUser error", e);
        } catch (ScriptException e) {
            logger.error("ScriptException checkUser error", e);
        }

    }

    public static String getQueueCount(Ticket ticket, TrainQuery query) {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.getQueueCount);

        httpPost.addHeader(CookieManager.cookieHeader());
        httpPost.setEntity(new StringEntity(getQueueCountParam(ticket, query), ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("getQueueCount error", e);
        }

        return result;
    }

    public static String confirmSingleForQueue(Ticket ticket, TrainQuery query) {
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(UrlConfig.confirmSingleForQueue);

        httpPost.addHeader(CookieManager.cookieHeader());
        httpPost.setEntity(new StringEntity(confirmSingleForQueueParam(ticket, query), ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

        String result = StringUtils.EMPTY;
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            CookieManager.touch(response);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("confirmSingleForQueue error", e);
        }

        return result;
    }

    public static String queryOrderWaitTime() {
        CloseableHttpClient httpClient = buildHttpClient();
        String url = UrlConfig.queryOrderWaitTime + "?" + queryOrderWaitTimeParam();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(CookieManager.cookieHeader());

        String result = StringUtils.EMPTY;
        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            logger.error("queryOrderWaitTime Interrupted error", e);
        }
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("queryOrderWaitTime error", e);
        }

        return result;
    }

    public static byte[] confirmTicketCaptchaImage() {
        return captchaImage(UrlConfig.confirmTicketCaptcha);
    }

    //******************************** 私有方法 ****************************************
    private static byte[] captchaImage(String url) {
        Objects.requireNonNull(url);

        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader(CookieManager.cookieHeader());

        byte[] result = new byte[0];
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            CookieManager.touch(response);
            result = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            logger.error("captchaImage error", e);
        }

        return result;
    }

    private static String queryOrderWaitTimeParam() {
        String token = Optional.ofNullable(ResultManager.get("repeatSubmitToken")).map(thrushResult -> (String)thrushResult.getValue()).orElse(StringUtils.EMPTY);

        StringBuilder builder = new StringBuilder();
        builder.append("random=").append(System.currentTimeMillis()).append("&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=").append(token);

        return builder.toString();
    }

    private static String confirmSingleForQueueParam(Ticket ticket, TrainQuery query) {
        String token = Optional.ofNullable(ResultManager.get("repeatSubmitToken")).map(thrushResult -> (String)thrushResult.getValue()).orElse(StringUtils.EMPTY);
        String purposeCodes = Optional.ofNullable(ResultManager.get("purposeCodes")).map(thrushResult -> (String)thrushResult.getValue()).orElse(StringUtils.EMPTY);
        String keyCheck = Optional.ofNullable(ResultManager.get("keyCheck")).map(thrushResult -> (String)thrushResult.getValue()).orElse(StringUtils.EMPTY);

        StringBuilder builder = new StringBuilder();
        try {
            builder.append("passengerTicketStr=").append(URLEncoder.encode(passengerTickets(query), "UTF-8")).append("&oldPassengerStr=").append(URLEncoder.encode(oldPassengers(query), "UTF-8"))
                    .append("&randCode=").append(randCode()).append("&purpose_codes=").append(purposeCodes).append("&key_check_isChange=").append(keyCheck).append("&leftTicketStr=").append(URLEncoder.encode(ticket.getLeftTicket(), "UTF-8"))
                    .append("&train_location=").append(ticket.getTrainLocation()).append("&choose_seats=&seatDetailType=").append(seatDetailType()).append("&roomType=00&dwAll=N&_json_att=&REPEAT_SUBMIT_TOKEN=").append(token);

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private static String seatDetailType() {
        String xNum = "0";//下铺
        String zNum = "0";//中铺
        String sNum = "0";//上铺
        return xNum + zNum + sNum;
    }

    private static String randCode() {
        return StringUtils.EMPTY;
    }

    private static String getQueueCountParam(Ticket ticket, TrainQuery query) {
        String token = Optional.ofNullable(ResultManager.get("repeatSubmitToken")).map(thrushResult -> (String)thrushResult.getValue()).orElse(StringUtils.EMPTY);
        String purposeCodes = Optional.ofNullable(ResultManager.get("purposeCodes")).map(thrushResult -> (String)thrushResult.getValue()).orElse(StringUtils.EMPTY);

        StringBuilder builder = new StringBuilder();
        try {
            builder.append("train_date=").append(formatDate(query)).append("&train_no=").append(ticket.getTrainLongNo()).append("&stationTrainCode=")
                    .append(ticket.getTrainNo()).append("&seatType=").append(SeatConfig.getSeatType(query.getSeat())).append("&fromStationTelecode=").append(ticket.getFromStationTelecode())
                    .append("&toStationTelecode=").append(ticket.getToStationTelecode()).append("&leftTicket=").append(URLEncoder.encode(ticket.getLeftTicket(), "UTF-8")).append("&purpose_codes=").append(purposeCodes)
                    .append("&train_location=").append(ticket.getTrainLocation()).append("&_json_att=&REPEAT_SUBMIT_TOKEN=").append(token);

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private static String formatDate(TrainQuery query) {
        LocalDate localDate = LocalDate.parse(query.getDate());
        try {
            return URLEncoder.encode(localDate.format(DateTimeFormatter.ofPattern("EEE MMM dd YYYY", Locale.ENGLISH)) + " 00:00:00 GMT+0800 (CST)", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String genCheckOrderInfoParam(TrainQuery query) {
        try {
            StringBuilder builder = new StringBuilder("cancel_flag=2&bed_level_order_num=000000000000000000000000000000&passengerTicketStr=");
            builder.append(URLEncoder.encode(passengerTickets(query), "UTF-8")).append("&oldPassengerStr=").append(URLEncoder.encode(oldPassengers(query), "UTF-8"))
                    .append("&tour_flag=dc&randCode=").append(randCode()).append("&_json_att=&REPEAT_SUBMIT_TOKEN=").append(Optional.ofNullable(ResultManager.get("repeatSubmitToken")).map(thrushResult -> (String)thrushResult.getValue()).orElse(StringUtils.EMPTY));
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String passengerTickets(TrainQuery query) {
        List<Passenger> passengers = Optional.ofNullable(ResultManager.get("passengers")).map(thrushResult -> (List<Passenger>)thrushResult.getValue()).orElse(new ArrayList<>());
        Passenger passenger = PassengerUtil.getPassenger(passengers, query);

        StringBuilder builder = new StringBuilder();
        builder.append(SeatConfig.getSeatType(query.getSeat())).append(",0,").append(passenger.getPassengerType()).append(",").append(passenger.getName())
                .append(",").append(passenger.getIdType()).append(",").append(passenger.getIdNo()).append(",").append(passenger.getMobile()).append(",N");

        return builder.toString();
    }

    private static String oldPassengers(TrainQuery query) {
        List<Passenger> passengers = Optional.ofNullable(ResultManager.get("passengers")).map(thrushResult -> (List<Passenger>)thrushResult.getValue()).orElse(new ArrayList<>());
        Passenger passenger = PassengerUtil.getPassenger(passengers, query);

        StringBuilder builder = new StringBuilder();
        builder.append(passenger.getName()).append(",").append(passenger.getIdType()).append(",").append(passenger.getIdNo()).append(",").append(passenger.getPassengerType()).append("_");

        return builder.toString();
    }

    private static String genSubmitOrderRequestParam(Ticket ticket, TrainQuery query) {
        StringBuilder builder = new StringBuilder();
        builder.append("secretStr=").append(ticket.getToken()).append("&train_date=").append(query.getDate()).append("&back_train_date=").append(query.getDate())
                .append("&tour_flag=dc&purpose_codes=ADULT&query_from_station_name=").append(query.getBegin()).append("&query_to_station_name=").append(query.getEnd())
                .append("&undefined");
        return builder.toString();
//        return "secretStr=6M83cI332lJQOYNmjw3FI67SVLruAvnrQrAS1yEV4XfMvgbkw%2B9WD9VKTQzMiz0yb2A2KBs5mopX%0A8DaZxzgGbuziE4t4kOiduHy%2Bn0I3p99ICc7QjE3Lu7OmuQt5Q8uxKH8BD3CklK1JL5LN9U9nldb3%0A7ts8bHhVQLkSdGdMSxlISdYZdzHK2kRxK2lCFyhQwmMDkmIoXHzake%2BSCQh5n50hLUWxiTx2eqDG%0AxHIbCnZbDSWV&train_date=2017-08-04&back_train_date=2017-07-27&tour_flag=dc&purpose_codes=ADULT&query_from_station_name=衡阳&query_to_station_name=长沙&undefined";
    }

    private static String genQueryParam(TrainQuery trainQuery) {
        StringBuilder builder = new StringBuilder();
        builder.append("leftTicketDTO.train_date=").append(trainQuery.getDate());
        builder.append("&leftTicketDTO.from_station=").append(Optional.ofNullable(StationNameUtil.findStation(trainQuery.getBegin())).map(station -> station.getCode()).orElse(StringUtils.EMPTY));
        builder.append("&leftTicketDTO.to_station=").append(Optional.ofNullable(StationNameUtil.findStation(trainQuery.getEnd())).map(station -> station.getCode()).orElse(StringUtils.EMPTY));
        builder.append("&purpose_codes=").append(trainQuery.getType());
        return builder.toString();
    }

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

    public static CloseableHttpClient buildHttpClient() {
        return HttpClients.custom().setConnectionManager(pools).setUserAgent(USER_AGENT).evictIdleConnections(30, TimeUnit.SECONDS).build();
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
