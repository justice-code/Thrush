package org.eddy;

import org.eddy.http.HttpRequest;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by eddy on 2017/7/5.
 */
public class InitTest {

    @Test
    public void test() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.init();
    }

    @Test
    public void test2() {
        String date = "2017-05-24";
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.toString());
        System.out.println(localDate.format(DateTimeFormatter.ofPattern("EEE MMM dd YYYY", Locale.ENGLISH)) + " 00:00:00 GMT+0800 (CST)");

//        LocalDateTime localDateTime = LocalDateTime.now();
//        System.out.println(localDateTime.toString());
    }
}
