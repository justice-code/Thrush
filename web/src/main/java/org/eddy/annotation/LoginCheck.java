package org.eddy.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginCheck {

    /**
     * 忽略的方法名
     * @return
     */
    String[] exclusions() default {};
}
