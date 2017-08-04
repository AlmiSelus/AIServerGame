package com.almi.games.server.board;

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
public class Board {
    private int rows;
    private int cols;
    private String[] availableFigures;
}
