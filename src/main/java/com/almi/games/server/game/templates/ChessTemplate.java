package com.almi.games.server.game.templates;

/**
 * Created by c309044 on 2017-08-02.
 */
public class ChessTemplate implements GameTemplate {


    @Override
    public int rows() {
        return 8;
    }

    @Override
    public int cols() {
        return 8;
    }

    @Override
    public String[] availableFigures() {
        return new String[] {
                //whites
                "\u2654", // white king
                "\u2655", // white queen
                "\u2656", // white rook
                "\u2657", // white bishop
                "\u2658", // white knight
                "\u2659", // white pawn

                //blacks
                "\u265A", // black king
                "\u265B", // black queen
                "\u265C", // black rook
                "\u265D", // black bishop
                "\u265E", // black knight
                "\u265F"  // black pawn
                };
    }
}
