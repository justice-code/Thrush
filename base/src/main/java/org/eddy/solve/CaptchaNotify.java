package org.eddy.solve;

import lombok.*;

/**
 * Created by Justice-love on 2017/7/16.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CaptchaNotify {

    private String pipeline;

    private Integer[] numbers;
}
