package org.eddy;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Created by Justice-love on 2017/7/27.
 */
public class DefaultStringFieldType implements FieldType<String, String> {
    @Override
    public String convert(String s) {
        return Optional.ofNullable(s).orElse(StringUtils.EMPTY);
    }
}
