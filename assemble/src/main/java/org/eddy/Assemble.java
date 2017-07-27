package org.eddy;

/**
 * Created by Justice-love on 2017/7/27.
 */
public @interface Assemble {

    String expression();

    Class<? extends FieldType> fieldType() default DefaultStringFieldType.class;
}
