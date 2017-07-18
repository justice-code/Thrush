package org.eddy.web;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Justice-love on 2017/7/18.
 */
@Getter @Setter @ToString
public class CaptchaResult {

    private String pipeline;

    private Integer[] numbers;
}
