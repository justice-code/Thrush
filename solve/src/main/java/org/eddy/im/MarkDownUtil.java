package org.eddy.im;

import com.alibaba.fastjson.JSONObject;
import org.eddy.config.HostConfig;

import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/21.
 */
public class MarkDownUtil {

    public static String createContent(String content, String pipeline) {
        Objects.requireNonNull(content);

        JSONObject jsonObject = template();
        StringBuilder builder = new StringBuilder("* ").append(content).append("\n");
        builder.append("* [刷新验证码](").append(HostConfig.domain).append("/task/refresh?pipeline=").append(pipeline).append(")\n");
        builder.append("* [输入验证码](").append(HostConfig.domain).append("/web/loginCaptcha?pipeline=").append(pipeline).append(")\n");
        jsonObject.put("text", builder.toString());
        return jsonObject.toJSONString();
    }

    public static String createContent(String content, String url, String pipeline) {
        Objects.requireNonNull(content);

        JSONObject jsonObject = template();
        StringBuilder builder = new StringBuilder("* ").append(content).append("\n");
        builder.append("* [刷新验证码](").append(HostConfig.domain).append("/task/refresh?pipeline=").append(pipeline).append(")\n");
        builder.append("* [输入验证码](").append(HostConfig.domain).append("/web/loginCaptcha?pipeline=").append(pipeline).append(")\n");
        builder.append("![验证码](").append(url).append(")");
        jsonObject.put("text", builder.toString());
        return jsonObject.toJSONString();
    }

    public static String begin() {
        JSONObject jsonObject = template();
        StringBuilder builder = new StringBuilder("* ").append("开启12306抢票任务").append("\n");
        builder.append("* [开始抢票](").append(HostConfig.domain).append("/task/begin").append(")\n");
        jsonObject.put("text", builder.toString());
        return jsonObject.toJSONString();
    }

    private static JSONObject template() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "12306购票提醒");
        return jsonObject;
    }
}
