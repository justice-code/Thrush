package org.eddy;

import org.eddy.pipeline.CoordinateUtil;
import org.junit.Test;

/**
 * Created by Justice-love on 2017/7/16.
 */
public class CoordinateTest {

    @Test
    public void test() {
        Integer[] integers = {2, 8};
        System.out.println(CoordinateUtil.computeCoordinate(integers));
    }
}
