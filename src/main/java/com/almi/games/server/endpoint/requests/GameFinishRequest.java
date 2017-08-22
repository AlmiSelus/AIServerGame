package com.almi.games.server.endpoint.requests;

import com.almi.games.server.game.GamePlayer;
import com.almi.games.server.game.GameStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Almi on 8/16/2017.
 */
@JsonSerialize
@JsonDeserialize
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameFinishRequest extends GameConnectionRequest {

    private GameStatus gameStatus;
    private String winner;

}
