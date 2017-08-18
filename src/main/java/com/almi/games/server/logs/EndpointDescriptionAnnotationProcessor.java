package com.almi.games.server.logs;

import org.apache.logging.log4j.util.Strings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Almi on 8/16/2017.
 */
public class EndpointDescriptionAnnotationProcessor {

    public static EndpointDescription getAnnotationData(Method method) {
        if(hasAnnotation(method)) {
            return method.getAnnotation(EndpointDescription.class);
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

    private static boolean hasAnnotation(Method method) {
        return method.isAnnotationPresent(EndpointDescription.class);
    }

}
