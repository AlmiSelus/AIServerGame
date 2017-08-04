package com.almi.games.server.game.templates;

/**
 * Created by c309044 on 2017-08-03.
 */
public interface GameTemplate {
    int rows();
    int cols();
    String[] availableFigures();
}
