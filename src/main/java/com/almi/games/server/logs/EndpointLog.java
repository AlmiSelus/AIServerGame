package com.almi.games.server.logs;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Created by Almi on 8/16/2017.
 */
@Builder(toBuilder = true)
@Getter
public class EndpointLog {

    public static final EndpointLog EMPTY = EndpointLog.builder().build();

    private String endpointPath;
    private String endpointConsumes;
    private String endpointProduces;
    private String endpointRequest;
    private String endpointDescription;
    private Set<ParameterLogHolder> endpointParameters;
    private ClassLogHolder endpointRestRequestBody;
    private ClassLogHolder endpointResponse;
    private String endpointName;
}

