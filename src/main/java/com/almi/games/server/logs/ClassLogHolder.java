package com.almi.games.server.logs;

import io.vavr.Tuple2;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Created by Almi on 8/17/2017.
 */
@Builder(toBuilder = true)
@Getter
public class ClassLogHolder {

    public static final ClassLogHolder EMPTY = ClassLogHolder.builder().build();

    private String className;
    private List<Tuple2<String, String>> fieldHolders;
}
