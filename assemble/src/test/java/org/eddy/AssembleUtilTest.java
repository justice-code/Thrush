package org.eddy;

import org.junit.Test;

import java.beans.IntrospectionException;

/**
 * Created by Justice-love on 2017/7/27.
 */
public class AssembleUtilTest {

    @Test
    public void test() throws IllegalAccessException, IntrospectionException, InstantiationException {
        String[] arr = {"a", "b", "c"};
        Te te = AssembleUtil.assemble(Te.class, arr);
        System.out.println(te);
    }
}
