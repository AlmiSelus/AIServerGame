package com.almi.games.server.ai.decisiontree;

import io.vavr.API;
import io.vavr.Tuple2;
import org.springframework.security.access.method.P;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import static io.vavr.API.$;
import static io.vavr.API.Case;

/**
 * Created by c309044 on 2017-08-25.
 */
public class PossibleIndicesMatrix extends ArrayList<Integer[]> {

    private StateTreeNode stateTreeNode;
    private List<Integer> visitedIndices = new LinkedList<>();

    private PossibleIndicesMatrix(StateTreeNode stateTreeNode) {
        this.stateTreeNode = stateTreeNode;
    }

    public static PossibleIndicesMatrix of(StateTreeNode stateTreeNode) {
        PossibleIndicesMatrix indices = new PossibleIndicesMatrix(stateTreeNode);
        indices.generate();
        return indices;
    }

    public static PossibleIndicesMatrix of(StateTreeNode stateTreeNode, int indicesValue) {
        PossibleIndicesMatrix indices = new PossibleIndicesMatrix(stateTreeNode);
        indices.generateOnlyForEq(indicesValue);
        return indices;
    }

    public Tuple2<Integer, Integer> getTuple(int index) {
        Integer[] indices = get(index);
        return new Tuple2<>(indices[0], indices[1]);
    }

    private void generateOnlyForEq(int e) {
        loopOver((row, col) -> API.Match(stateTreeNode.getState().getInt(row, col) == e).of(
            Case($(true), ()-> add(new Integer[]{row, col})),
            Case($(), ()->false)
        ));
    }

    private void generate() {
        loopOver((row, col)-> add(new Integer[]{row, col}));
    }

    private void loopOver(BiFunction<Integer, Integer, Object> action) {
        for(int row = 0; row < stateTreeNode.getState().rows(); ++row) {
            for(int col = 0; col < stateTreeNode.getState().columns(); ++col) {
                action.apply(row, col);
            }
        }
    }

    public void visit(int indicesIndex) {
        visitedIndices.add(indicesIndex);
    }

    public boolean wasVisited(int indicesIndex) {
        return visitedIndices.contains(indicesIndex);
    }
}
