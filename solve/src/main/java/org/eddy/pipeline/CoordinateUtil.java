package org.eddy.pipeline;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Justice-love on 2017/7/16.
 */
public class CoordinateUtil {

    private static final int LENGTH = 293;
    private static final int HEIGHT = 150;

    public static String computeCoordinate(Integer[] numbers) {
        Objects.requireNonNull(numbers);

        List<String> coordinates = Arrays.stream(numbers).map(number -> Optional.ofNullable(number).map(n -> {
            int l = n % 4 == 0 ? 4 : n % 4;
            int length = LENGTH / 8 * (l * 2 - 1);
            int h = n / 4 == 2 ? 2 : (n / 4) + 1;
            int height = HEIGHT / 4 * (h * 2 - 1);
            return length + "," + height;
        }).orElse("0,0")).collect(Collectors.toList());

        return String.join(",", coordinates);
    }

}
