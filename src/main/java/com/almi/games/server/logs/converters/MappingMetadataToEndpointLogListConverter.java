package com.almi.games.server.logs.converters;

import com.almi.games.server.logs.EndpointDescription;
import com.almi.games.server.logs.beans.ClassLogHolder;
import com.almi.games.server.logs.beans.EndpointLog;
import com.almi.games.server.logs.beans.ParameterLogHolder;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Almi on 8/18/2017.
 *
 * Utility class that serves as a processor
 */
@Slf4j
@Component
public class MappingMetadataToEndpointLogListConverter implements Converter<RequestMappingHandlerMapping, List<EndpointLog>> {

    /**
     * Separator for common data in view
     */
    private static final String ENDPOINT_DATA_SEPARATOR = ", ";

    @Autowired
    private MethodToEndpointDescriptionConverter methodDescriptionConverter;

    /**
     * Filter check if there is a path for given endpoint that has /api/game as some arbitrary substring
     */
    private Predicate<Map.Entry<RequestMappingInfo, HandlerMethod>> endpointsFilter =
            entry -> entry.getKey().getPatternsCondition().getPatterns().stream()
                    .collect(Collectors.joining()).contains("/api/game");

    /**
     * Method that creates all endpoint logs. It collects data for each endpoint in EndpointLog object.
     *
     * @return all endpoints logs
     */
    @Override
    public List<EndpointLog> convert(RequestMappingHandlerMapping source) {
        return source.getHandlerMethods().entrySet().stream()
                .filter(endpointsFilter)
                .map(requestMappingEntry ->
                        populateLogWithRequestMappingData(
                                populateEndpointLogWithMethodData(EndpointLog.EMPTY, requestMappingEntry.getValue()),
                                requestMappingEntry.getKey()))
                .sorted((o1, o2) -> o1.getEndpointPath().compareToIgnoreCase(o2.getEndpointPath()))
                .collect(Collectors.toList());
    }

    private EndpointLog populateEndpointLogWithMethodData(EndpointLog endpointLog, HandlerMethod handlerMethod) {
        endpointLog = populateEndpointLogWithDescriptionData(endpointLog,
                methodDescriptionConverter.convert(handlerMethod.getMethod()), handlerMethod);

        return endpointLog.toBuilder()
                .endpointParameters(createMethodParametersLog(handlerMethod))
                .endpointRestRequestBody(createRequestBodyLog(handlerMethod))
                .endpointResponse(processClassToLog(getTypeClass(handlerMethod)))
                .build();
    }

    private EndpointLog populateLogWithRequestMappingData(EndpointLog log, RequestMappingInfo requestMapping) {
        return log.toBuilder()
                .endpointPath(requestMapping.getPatternsCondition().getPatterns().stream().collect(Collectors.joining(ENDPOINT_DATA_SEPARATOR)))
                .endpointRequest(requestMapping.getMethodsCondition().getMethods().stream().map(Enum::toString).collect(Collectors.joining(ENDPOINT_DATA_SEPARATOR)))
                .endpointConsumes(requestMapping.getConsumesCondition().getConsumableMediaTypes().stream().map(MimeType::toString).collect(Collectors.joining(ENDPOINT_DATA_SEPARATOR)))
                .endpointProduces(requestMapping.getProducesCondition().getProducibleMediaTypes().stream().map(MimeType::toString).collect(Collectors.joining(ENDPOINT_DATA_SEPARATOR)))
                .build();
    }

    private ClassLogHolder createRequestBodyLog(HandlerMethod handlerMethod) {
        return Arrays.stream(handlerMethod.getMethodParameters())
                .filter(methodParameter -> methodParameter.hasParameterAnnotation(RequestBody.class))
                .map(methodParameter -> processClassToLog(methodParameter.getParameterType()))
                .findFirst().orElse(ClassLogHolder.EMPTY);
    }

    private String createEndpointName(EndpointDescription description, HandlerMethod handlerMethod) {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(StringUtils.capitalize(
                description.name().trim().isEmpty() ?
                        handlerMethod.getMethod().getName().replace("get", StringUtils.EMPTY) : description.name()
        )), StringUtils.SPACE);
    }

    private Set<ParameterLogHolder> createMethodParametersLog(HandlerMethod handlerMethod) {
        return Arrays.stream(handlerMethod.getMethodParameters())
                .filter(methodParameter -> methodParameter.hasParameterAnnotation(PathVariable.class))
                .map(methodParameter -> {
                    PathVariable pathVariable = methodParameter.getParameterAnnotation(PathVariable.class);
                    return ParameterLogHolder.builder()
                            .parameterName(pathVariable.value().isEmpty() ? pathVariable.name() : pathVariable.value())
                            .possibleValues(Enum.class.isAssignableFrom(methodParameter.getParameterType()) ?
                                    Arrays.stream(methodParameter.getParameterType().getEnumConstants()).map(Object::toString)
                                            .collect(Collectors.toSet()) : Collections.emptySet())
                            .parameterClass(methodParameter.getParameterType().getSimpleName())
                            .build();
                })
                .collect(Collectors.toSet());
    }

    private EndpointLog populateEndpointLogWithDescriptionData(EndpointLog log, EndpointDescription description, HandlerMethod handlerMethod) {
        if(!description.description().equalsIgnoreCase(Strings.EMPTY)) {
            log = log.toBuilder()
                    .endpointDescription(description.description())
                    .build();
        }

        return log.toBuilder()
                .endpointName(createEndpointName(description, handlerMethod))
                .build();
    }

    private ClassLogHolder processClassToLog(Class<?> clz) {
        return ClassLogHolder.builder()
                .className(clz.getSimpleName())
                .fieldHolders(Arrays.stream(FieldUtils.getAllFields(clz))
                        .map(field-> new Tuple2<>(field.getType().getSimpleName(), field.getName()))
                        .collect(Collectors.toList()))
                .build();
    }

    private Class<?> getTypeClass(HandlerMethod handlerMethod) {
        return getTypeClass(handlerMethod.getReturnType().getGenericParameterType());
    }

    private Class<?> getTypeClass(Type type) {
        if(type instanceof ParameterizedType) {
            return getTypeClass(((ParameterizedType)type).getActualTypeArguments()[0]);
        }

        return (Class<?>)type;
    }

}
