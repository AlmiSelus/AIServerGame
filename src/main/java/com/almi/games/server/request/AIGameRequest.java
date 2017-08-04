package com.almi.games.server.request;

import com.almi.games.server.game.GameStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by c309044 on 2017-08-02.
 */
@JsonSerialize
@JsonDeserialize
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AIGameRequest {
    /**
     * Caller ID
     */
    private String userID;

    /**
     * Position - general case for position based games
     */
    private String position;

    /**
     * Request timestamp
     */
    private LocalDateTime timestamp;

    /**
     * Status of the game
     */
    private GameStatus gameStatus;

    /**
     * Game ID
     */
    private Long gameId;

    /**
     * Move col
     */
    private Integer col;

    /**
     * Move row
     */
    private Integer row;

}
