package org.eddy.cookie;

import org.eddy.ApplicationStart;
import org.eddy.http.HttpRequest;
import org.eddy.util.StationNameUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Justice-love on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationStart.class})
public class StationNameTest {

    @Test
    public void test() {
        String js = HttpRequest.stationName();
        StationNameUtil.parse(js);
    }
}
