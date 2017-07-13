package org.eddy.cookie;

import org.eddy.ApplicationStart;
import org.eddy.http.HttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by Justice-love on 2017/7/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationStart.class})
public class CookieTest {

    @Autowired
    private HttpRequest httpRequest;

    @Test
    public void test1() throws IOException {
        httpRequest.init();
    }

}
