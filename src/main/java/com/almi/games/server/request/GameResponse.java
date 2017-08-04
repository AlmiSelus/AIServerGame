package com.almi.games.server.request;

import com.almi.games.server.board.Board;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by c309044 on 2017-08-02.
 */
@JsonSerialize
@JsonDeserialize
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameResponse {
    private Board board;
}
