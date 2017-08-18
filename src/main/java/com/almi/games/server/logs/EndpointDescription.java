package com.almi.games.server.logs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Almi on 8/16/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointDescription {
    String description() default "";
    String name() default "";
}
