package org.eddy;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Justice-love on 2017/7/27.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Assemble {

    String expression();

    Class<? extends FieldType> fieldType() default DefaultStringFieldType.class;
}
