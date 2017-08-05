package org.eddy.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;

import java.util.Objects;
import java.util.Optional;

public class JsonUtil {

    public static String extract(String ognl, String json) throws OgnlException {
        Objects.requireNonNull(json);
        Objects.requireNonNull(ognl);

        JSONObject jsonObject = JSONObject.parseObject(json);
        Object value = Ognl.getValue(ognl, jsonObject);

        return Optional.ofNullable(value).map(s -> s.toString()).orElse(StringUtils.EMPTY);
    }

}
