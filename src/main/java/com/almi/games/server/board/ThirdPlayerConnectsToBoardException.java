package com.almi.games.server.board;

/**
 * Created by c309044 on 2017-08-04.
 */
public class ThirdPlayerConnectsToBoardException extends Exception {

    public ThirdPlayerConnectsToBoardException() {
        super("The game is already played by two users!");
    }
}
