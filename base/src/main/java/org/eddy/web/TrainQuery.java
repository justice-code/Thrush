package org.eddy.web;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Justice-love on 2017/7/26.
 */
@Getter @Setter
public class TrainQuery {
    private String date;
    private String begin;
    private String end;
    private String passenger;
    private String train;
    private String type = "ADULT";

}
