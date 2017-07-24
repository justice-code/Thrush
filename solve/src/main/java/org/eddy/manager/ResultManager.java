package org.eddy.manager;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.eddy.solve.ResultKey;
import org.eddy.solve.ThrushResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Justice-love on 2017/7/24.
 */
public class ResultManager {

    private static final Logger logger = LoggerFactory.getLogger(ResultManager.class);

    private static ThreadLocal<Set<ThrushResult>> resultManager = new InheritableThreadLocal<>();

    public static void touch(String result, ResultKey resultKey) {
        Objects.requireNonNull(resultKey);
        Objects.requireNonNull(result);

        try {

            JSONObject jsonObject = JSONObject.parseObject(result);
            String value = (String) Ognl.getValue(resultKey.getOgnl(), jsonObject);
            ThrushResult thrushResult = new ThrushResult(resultKey.getKey(), value);

            Set<ThrushResult> thrushResults = Optional.ofNullable(resultManager.get()).orElse(new HashSet<>());

            if (thrushResults.contains(thrushResult)) {
                thrushResults.remove(thrushResult);
            }
            thrushResults.add(thrushResult);
            resultManager.set(thrushResults);
        } catch (OgnlException e) {
            logger.error("ResultManager ognl error", e);
            throw new RuntimeException(e);
        }
    }


    public static ThrushResult get(String key) {
        Set<ThrushResult> thrushResults = Optional.ofNullable(resultManager.get()).orElse(new HashSet<>());

        ThrushResult thrushResult = new ThrushResult(key, null);
        return thrushResults.stream().filter(t -> Objects.equals(t, key)).findFirst().orElse(null);
    }
}
