package org.eddy.pipeline.command;

import lombok.*;

/**
 * Created by Justice-love on 2017/7/16.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CommandNotify {

    protected Command command;

    protected String pipeline;

    protected Object arg;
}
