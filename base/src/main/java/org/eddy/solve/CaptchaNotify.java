package org.eddy.solve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Justice-love on 2017/7/16.
 */
@Getter @Setter @AllArgsConstructor
public class CaptchaNotify {

    private String pipeline;

    private Integer[] number;
}
