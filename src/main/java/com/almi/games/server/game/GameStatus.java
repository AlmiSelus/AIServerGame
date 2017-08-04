package com.almi.games.server.game;

import java.util.Arrays;

/**
 * Created by c309044 on 2017-08-03.
 */
public enum GameStatus {
    UNDEFINED("undefined"), STARTED("started"), IN_PROGRESS("in_progress"), FINISHED("finished");

    private String statusText;

    GameStatus(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        return statusText;
    }

    public static GameStatus of(String status) {
        return Arrays.stream(values()).filter(gameStatus -> gameStatus.statusText.equalsIgnoreCase(status)).findFirst().orElse(UNDEFINED);
    }
}
