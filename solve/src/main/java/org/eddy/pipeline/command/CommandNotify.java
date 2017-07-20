package org.eddy.pipeline.command;

import lombok.*;

/**
 * Created by Justice-love on 2017/7/16.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CommandNotify {

    private Command command;

    private String pipeline;

    private Object arg;
}
