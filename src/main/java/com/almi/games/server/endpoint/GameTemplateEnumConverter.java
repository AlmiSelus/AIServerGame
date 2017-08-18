package com.almi.games.server.endpoint;

import com.almi.games.server.game.templates.GameTemplateEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Almi on 8/16/2017.
 */
@Component
public class GameTemplateEnumConverter implements Converter<String, GameTemplateEnum> {
    @Override
    public GameTemplateEnum convert(String source) {
        return GameTemplateEnum.of(source);
    }
}
