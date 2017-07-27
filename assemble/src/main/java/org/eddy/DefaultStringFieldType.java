package org.eddy;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Created by Justice-love on 2017/7/27.
 */
public class DefaultStringFieldType implements FieldType<String> {
    @Override
    public String convert(Object s) {
        return Optional.ofNullable(s).map(m -> m.toString()).orElse(StringUtils.EMPTY);
    }
}
