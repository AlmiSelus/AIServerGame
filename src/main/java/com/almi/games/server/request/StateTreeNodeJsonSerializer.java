package com.almi.games.server.request;

import com.almi.games.server.ai.decisiontree.StateTreeNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by c309044 on 2017-08-28.
 */
@Slf4j
@JsonComponent
public class StateTreeNodeJsonSerializer extends JsonSerializer<StateTreeNode> {
    @Override
    public void serialize(StateTreeNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if(value == null) {
            return;
        }

        if(value.getState() == null) {
            return;
        }

        gen.writeStartArray();
            serializeTree(value, gen, serializers);
        gen.writeEndArray();

    }

    private void serializeTree(StateTreeNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeArrayFieldStart("state");
            Arrays.stream(value.toString().substring(1, value.toString().length()-1).split(","))
                .map(String::trim).mapToInt(Integer::parseInt).forEachOrdered(i->{
                try {
                    gen.writeNumber(i);
                } catch (IOException e) {
                    log.info(ExceptionUtils.getStackTrace(e));
                }
            });
        gen.writeEndArray();

        if(value.getChildren().size() > 0) {
            gen.writeArrayFieldStart("children");
            for(StateTreeNode child : value.getChildren()) {
                serializeTree(child, gen, serializers);
            }
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }

}
