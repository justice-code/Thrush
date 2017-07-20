package org.eddy.cookie;

import org.apache.commons.io.FileUtils;
import org.eddy.ApplicationStart;
import org.eddy.http.HttpRequest;
import org.eddy.pipeline.CoordinateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justice-love on 2017/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationStart.class})
public class LoginTest {

    @Test
    public void test1() throws IOException {
        HttpRequest.init();
        HttpRequest.auth();
        byte[] bytes = HttpRequest.loginCaptchaImage();
        FileUtils.writeByteArrayToFile(new File("/Users/eddy/Desktop/image.jpg"), bytes);
    }

    @Test
    public void test2() throws IOException {
        byte[] bytes = HttpRequest.loginCaptchaImage();
        FileUtils.writeByteArrayToFile(new File("/Users/eddy/Desktop/image.jpg"), bytes);
    }

    @Test
    public void test3() {
        HttpRequest.init();
        HttpRequest.auth();
        byte[] bytes = HttpRequest.loginCaptchaImage();
        HttpRequest.checkRandCode(CoordinateUtil.computeCoordinate(new Integer[]{1,3}));
    }

    @Test
    public void test4() {
        HttpRequest.checkRandCode(CoordinateUtil.computeCoordinate(new Integer[]{1,3}));
    }

}
