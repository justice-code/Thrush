package org.eddy.util;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Justice-love on 2017/7/28.
 */
public class TokenUtil {

    public static String getToken(String html) throws ScriptException {
        Document document = Jsoup.parse(html);

        Elements elements = document.getElementsByTag("script");
        String jsContent = elements.stream().filter(e -> e.data().contains("globalRepeatSubmitToken") && e.childNodes().size() > 0)
                .findFirst().map(e -> e.childNode(0).outerHtml()).orElse(StringUtils.EMPTY);

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");
        engine.eval(jsContent);
        return (String) engine.get("globalRepeatSubmitToken");
    }

    public static String getPurposeCodes(String html) throws ScriptException {
        Document document = Jsoup.parse(html);

        Elements elements = document.getElementsByTag("script");
        String jsContent = elements.stream().filter(e -> e.data().contains("ticketInfoForPassengerForm") && e.childNodes().size() > 0)
                .findFirst().map(e -> e.childNode(0).outerHtml()).orElse(StringUtils.EMPTY);
        jsContent = jsContent.substring(0, jsContent.lastIndexOf("var"));

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");
        engine.eval(jsContent);
        ScriptObjectMirror objectMirror = (ScriptObjectMirror) engine.get("ticketInfoForPassengerForm");
        return (String) objectMirror.get("purpose_codes");
    }
}
