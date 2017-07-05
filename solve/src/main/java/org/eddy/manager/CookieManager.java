package org.eddy.manager;

import org.eddy.solve.ThrushCookie;

import java.util.List;

/**
 * Created by Justice-love on 2017/7/5.
 */
public class CookieManager {

    private static ThreadLocal<List<ThrushCookie>> cookieManager = new InheritableThreadLocal<List<ThrushCookie>>();
}
