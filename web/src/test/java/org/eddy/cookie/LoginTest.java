package org.eddy.cookie;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eddy.ApplicationStart;
import org.eddy.http.HttpRequest;
import org.eddy.pipeline.CoordinateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

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

    @Test
    public void test5() {
        HttpRequest.refreshLoginCaptchaImage();
        HttpRequest.refreshLoginCaptchaImage();
    }

    @Test
    public void test6() throws Exception {
        Document document = Jsoup.parse(FileUtils.readFileToString(new File("/Users/eddy/Desktop/content")));
        Elements elements = document.getElementsByTag("script");
        String result = elements.stream().filter(e -> e.data().contains("globalRepeatSubmitToken") && e.childNodes().size() > 0)
                .findFirst().map(e -> e.childNode(0).outerHtml()).orElse(StringUtils.EMPTY);

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");
        engine.eval(result);
        Object o = engine.get("globalRepeatSubmitToken");
        System.out.println(o);

    }

}
