package com.almi.games.server.logs.converters;

import com.almi.games.server.logs.EndpointDescription;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Almi on 8/16/2017.
 */
@Component
public class MethodToEndpointDescriptionConverter implements Converter<Method, EndpointDescription> {

    @Override
    public EndpointDescription convert(Method source) {
        if(hasAnnotation(source)) {
            return source.getAnnotation(EndpointDescription.class);
        }
        return new EndpointDescription(){
            @Override
            public String description() {
                return Strings.EMPTY;
            }

            @Override
            public String name() {
                return Strings.EMPTY;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return EndpointDescription.class;
            }
        };
    }

    private boolean hasAnnotation(Method method) {
        return method.isAnnotationPresent(EndpointDescription.class);
    }

}
