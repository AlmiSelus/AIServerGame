package com.almi.games.server.game.templates;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

/**
 * Created by c309044 on 2017-08-02.
 */
public class GameTemplateFactory {
    public static GameTemplate of(GameTemplateEnum template) {
        return Match(template).of(
                Case($(GameTemplateEnum.TIC_TAC_TOE), i -> new TicTacToeTemplate()),
                Case($(GameTemplateEnum.CHESS), i-> new ChessTemplate()),
                Case($(), i-> new TicTacToeTemplate())
        );
    }
}
