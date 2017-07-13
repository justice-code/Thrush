package org.eddy.manager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.eddy.solve.ThrushCookie;

import java.util.List;

/**
 * Created by Justice-love on 2017/7/5.
 */
public class CookieManager {

    private static final String COOKIE_HEADER = "Set-Cookie";

    private static ThreadLocal<List<ThrushCookie>> cookieManager = new InheritableThreadLocal<List<ThrushCookie>>();

    public static void touch(HttpResponse response) {
        Header[] headers = response.getHeaders(COOKIE_HEADER);

    }
}
