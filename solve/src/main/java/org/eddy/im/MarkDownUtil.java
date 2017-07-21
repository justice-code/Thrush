package org.eddy.im;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/21.
 */
public class MarkDownUtil {

    public static String createContent(String content) {
        Objects.requireNonNull(content);

        JSONObject jsonObject = template();
        jsonObject.put("text", "* " + content);
        return jsonObject.toJSONString();
    }

    public static String createContent(String content, String url) {
        Objects.requireNonNull(content);

        JSONObject jsonObject = template();
        StringBuilder builder = new StringBuilder("* ").append(content).append("\n");
        builder.append("![验证码](").append(url).append(")");
        jsonObject.put("text", builder.toString());
        return jsonObject.toJSONString();
    }

    private static JSONObject template() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "12306购票提醒");
        return jsonObject;
    }
}
