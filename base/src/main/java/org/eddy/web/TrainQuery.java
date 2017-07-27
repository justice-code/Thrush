package org.eddy.web;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Justice-love on 2017/7/26.
 */
@Getter @Setter @ToString(exclude = {"aLong", "date", "type"})
public class TrainQuery {
    private String date;
    private String begin;
    private String end;
    private String passenger;
    private String train;
    private String type = "ADULT";
    private AtomicLong aLong = new AtomicLong(0);
}
