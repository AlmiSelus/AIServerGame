package com.almi.games.server.ai.decisiontree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Almi on 8/24/2017.
 */
@Getter
@Builder(toBuilder = true)
public class StateTreeNode {

    public static final StateTreeNode EMPTY = builder().build();

    @Builder.Default
    private INDArray state = Nd4j.zeros(3, 3);
    @Builder.Default
    private Set<StateTreeNode> children = new LinkedHashSet<>();

    @JsonIgnore
    private StateTreeNode parent;

    public boolean hasState(INDArray state) {
        boolean hasSuchState = false;

        for(StateTreeNode child : children) {
            if(child.getState().equals(state)) {
                hasSuchState = true;
                break;
            }
        }

        return hasSuchState;
    }

    public static StateTreeNode parseSingleString(String stateString) {
        StateTreeNode stateTreeNode = StateTreeNode.EMPTY;
        int[] arr = Arrays.stream(stateString.substring(1, stateString.length()-1).split(","))
                .map(String::trim).mapToInt(Integer::parseInt).toArray();
        for(int i = 0; i < arr.length; ++i) {
            stateTreeNode.getState().put(i/3, i%3, arr[i]);
        }
        return stateTreeNode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for(int row = 0; row < state.rows(); ++row) {
            for(int col = 0; col < state.columns(); ++col) {
                sb.append(state.getInt(row, col)).append(", ");
            }
        }
        sb.replace(sb.length()-2, sb.length(), "");
        sb.append("]");
        return sb.toString();
    }
}
