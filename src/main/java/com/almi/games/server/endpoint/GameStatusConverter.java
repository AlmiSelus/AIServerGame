package com.almi.games.server.endpoint;

import com.almi.games.server.game.GameStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Almi on 8/16/2017.
 */
@Component
public class GameStatusConverter implements Converter<String, GameStatus> {
    @Override
    public GameStatus convert(String source) {
        return GameStatus.of(source);
    }
}
