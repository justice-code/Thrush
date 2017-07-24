package org.eddy;

import org.eddy.listener.ThrushApplicationListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * Created by Justice-love on 2017/7/13.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class ApplicationStart {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApplicationStart.class);
        springApplication.addListeners(new ApplicationPidFileWriter("thrush.pid"));
        springApplication.addListeners(new ThrushApplicationListener());
        springApplication.run(args);
    }
}
