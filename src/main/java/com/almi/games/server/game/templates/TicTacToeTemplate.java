package com.almi.games.server.game.templates;

/**
 * Created by c309044 on 2017-08-02.
 */
public class TicTacToeTemplate implements GameTemplate {

    @Override
    public int rows() {
        return 3;
    }

    @Override
    public int cols() {
        return 3;
    }

    @Override
    public String[] availableFigures() {
        return new String[] { "X", "O" };
    }
}
