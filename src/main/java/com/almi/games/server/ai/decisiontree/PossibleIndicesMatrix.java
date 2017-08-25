package com.almi.games.server.ai.decisiontree;

import io.vavr.Tuple2;

import java.util.ArrayList;

/**
 * Created by c309044 on 2017-08-25.
 */
public class PossibleIndicesMatrix extends ArrayList<Integer[]> {

    private PossibleIndicesMatrix(int rows, int cols) {
        generate(rows, cols);
    }

    public static PossibleIndicesMatrix of(int rows, int cols) {
        return new PossibleIndicesMatrix(rows, cols);
    }

    public Tuple2<Integer, Integer> getTuple(int index) {
        Integer[] indices = get(index);
        return new Tuple2<>(indices[0], indices[1]);
    }

    private void generate(int rows, int cols) {
        for(int row = 0; row < rows; ++row) {
            for(int col = 0; col < cols; ++col) {
                add(new Integer[]{row, col});
            }
        }
    }
}
