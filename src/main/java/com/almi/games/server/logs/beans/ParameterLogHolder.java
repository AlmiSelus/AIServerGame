package com.almi.games.server.logs.beans;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Created by Almi on 8/18/2017.
 */
@Builder
@Getter
public class ParameterLogHolder {
    private String parameterName;
    private String parameterClass;
    private Set<String> possibleValues;
}
