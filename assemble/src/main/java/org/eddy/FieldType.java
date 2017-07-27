package org.eddy;

/**
 * Created by Justice-love on 2017/7/27.
 */
public interface FieldType<R,T> {

    T convert(R r);
}
