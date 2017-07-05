package org.eddy;

import org.eddy.http.HttpUtil;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by eddy on 2017/7/5.
 */
public class InitTest {

    @Test
    public void test() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.init();
    }
}
