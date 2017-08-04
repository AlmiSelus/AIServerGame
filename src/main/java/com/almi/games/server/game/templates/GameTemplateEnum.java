package com.almi.games.server.game.templates;

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
        for(GameTemplateEnum gameTemplate : values()) {
            if(gameTemplate.type.equalsIgnoreCase(type)) {
                return gameTemplate;
            }
        }
        return TIC_TAC_TOE;
    }
}
