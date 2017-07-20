package org.eddy.pipeline;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.eddy.http.HttpRequest;
import org.eddy.pipeline.command.CommandNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/17.
 */
@Getter @Setter @NoArgsConstructor
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotifyRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NotifyRunnable.class);
    // 3分钟
    private static final int DEFAULT_TIME = 3;

    private String pipelineGroup;

    @Autowired
    private Pipeline pipeline;

    @Autowired
    private HttpRequest httpRequest;

    @Override
    public void run() {
        CommandNotify init = findLoginNotify();
        init.getCommand().execute(httpRequest, init.getArg());
        
        CommandNotify loginNotify = findLoginNotify();
        loginNotify.getCommand().execute(httpRequest, loginNotify.getArg());

    }

    //***************************************************** private *******************************************************

    private String imageFileName() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.toLocalDate().toString() + "/" + dateTime.toLocalTime().withNano(0).format(DateTimeFormatter.ISO_LOCAL_TIME) + ".jpg";
    }

    private CommandNotify findLoginNotify() {
        CommandNotify notify = pipeline.pollNotify(DEFAULT_TIME);
        Objects.requireNonNull(notify);

        if (StringUtils.equals(notify.getPipeline(), this.getPipelineGroup())) {
            return notify;
        }

        try {
            pipeline.putNotify(notify);
            Thread.sleep(300);
            return findLoginNotify();
        } catch (InterruptedException e) {
            throw new RuntimeException("findNotify error", e);
        }
    }
}
