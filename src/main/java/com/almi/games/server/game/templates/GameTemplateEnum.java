package com.almi.games.server.game.templates;

import java.util.Arrays;

/**
 * Created by c309044 on 2017-08-02.
 */
public enum GameTemplateEnum {
    TIC_TAC_TOE("tictactoe"), CHESS("chess");

    private String type;

    GameTemplateEnum(String type) {
        this.type = type;
    }

    public static GameTemplateEnum of(String type) {
        return Arrays.stream(values()).filter(gameTemplateEnum -> gameTemplateEnum.type.equalsIgnoreCase(type)).findFirst().orElse(TIC_TAC_TOE);
    }

    @Override
    public String toString() {
        return type;
    }
}
