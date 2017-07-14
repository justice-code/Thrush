package org.eddy.manager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeader;
import org.eddy.solve.ThrushCookie;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Justice-love on 2017/7/5.
 */
public class CookieManager {

    private static final String COOKIE_HEADER = "Set-Cookie";
    private static final String COOKIE ="Cookie";

    private static ThreadLocal<Set<ThrushCookie>> cookieManager = new InheritableThreadLocal<>();

    public static void touch(HttpResponse response) {
        Header[] headers = response.getHeaders(COOKIE_HEADER);
        Set<ThrushCookie> cookies = Arrays.stream(headers).map(header -> {
            String cookie = header.getValue().split(";")[0];
            String[] kv = cookie.trim().split("=");
            return new ThrushCookie(kv[0], kv[1]);
        }).collect(Collectors.toSet());
        Set<ThrushCookie> result = Optional.ofNullable(cookieManager.get()).orElse(new HashSet<>());
        result.addAll(cookies);
        cookieManager.set(cookies);
    }

    public static Header cookieHeader() {
        List<String> cookies = Optional.ofNullable(cookieManager.get()).orElse(new HashSet<>()).stream().map(cookie -> cookie.getKey() + "=" + cookie.getValue() + ";").collect(Collectors.toList());
        return new BasicHeader(COOKIE, String.join(" ", cookies));
    }


}
