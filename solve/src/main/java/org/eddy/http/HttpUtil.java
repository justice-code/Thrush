package org.eddy.http;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by Justice-love on 2017/7/5.
 */
public class HttpUtil {

    private static PoolingHttpClientConnectionManager pools;

    static {
        if (pools == null) {
            pools = new PoolingHttpClientConnectionManager();
            pools.setMaxTotal(50);
            pools.setDefaultMaxPerRoute(5);
        }
    }


}
