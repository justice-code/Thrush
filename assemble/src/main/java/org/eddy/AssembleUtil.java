package org.eddy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Justice-love on 2017/7/27.
 */
public class AssembleUtil {

    public static <T> T assemble(Class<T> type, Object param) throws IntrospectionException, IllegalAccessException, InstantiationException {
        Objects.requireNonNull(param);
        Objects.requireNonNull(type);

        T t = type.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        Arrays.stream(propertyDescriptors).forEach(p -> {
            try {
                if (StringUtils.equals("class", p.getDisplayName())) {
                    return;
                }

                Field field = type.getDeclaredField(p.getDisplayName());
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Assemble.class)) {
                    return;
                }

                Assemble assemble = field.getAnnotation(Assemble.class);
                Object value = getFieldType(assemble).convert(script(assemble.expression(), param));
                p.getWriteMethod().invoke(t, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return t;
    }

    private static FieldType getFieldType(Assemble assemble) throws IllegalAccessException, InstantiationException {
        return assemble.fieldType().newInstance();
    }

    private static Object script(String script, Object param) {
        Objects.requireNonNull(script);

        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        shell.setVariable("param", param);

        return shell.evaluate(script);
    }
}
