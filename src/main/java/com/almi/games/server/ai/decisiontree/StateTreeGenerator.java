package com.almi.games.server.ai.decisiontree;

import com.almi.games.server.ai.Generator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.random.RandomGenerator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created by Almi on 8/24/2017.
 */
@Slf4j
@Component
public class StateTreeGenerator implements Generator<StateTreeNode> {

    @Autowired
    private RandomGenerator randomGenerator;

    private Function<Boolean, Integer> ticOrToe = (i)-> i ? 2 : 1;

    @Override
    public StateTreeNode generate() {
        StateTreeNode root = StateTreeNode.builder().build();
//        generateWithDepth(0, 1, root);
        generateTree(root, 0, 2);
        return findRoot(root);
    }

    private void generateWithDepth(int currentDepth, int maxDepth, StateTreeNode parent) {
        if(maxDepth == currentDepth) {
            return;
        }

        StateTreeNode newStateNode = generateState(parent);

        parent.getChildren().add(newStateNode);
        Set<StateTreeNode> childrenStates = newStateNode.getChildren();
        for(int i = 0; i < countNum(newStateNode.getState(), 0); ++i) {
            generateWithDepth(currentDepth+1, maxDepth, newStateNode);
        }
        newStateNode.toBuilder().children(childrenStates).build();
    }

    /**
     * 1. Count how much states need to be generated
     * 2. Generate new state
     * 3. Add state as children to current state node
     * 4. Repeat for all states until given depth found
     */
    private void generateTree(StateTreeNode root, int currentLevel, int maxLevel) {
        if(currentLevel == maxLevel) {
            return;
        }
        for(int i = 0; i < countNum(root.getState(), 0); ++i) {
            StateTreeNode newStateNode = generateState(root);
            root.getChildren().add(newStateNode);
            log(newStateNode, currentLevel);
            for(int j = 0; j < countNum(newStateNode.getState(), 0); ++j) {
                generateTree(newStateNode, currentLevel+1, maxLevel);
            }
        }
    }

    private void log(StateTreeNode newStateNode, int currentDepth) {
        log.info( "Depth: {}, state: {}, elements: {}", currentDepth, newStateNode.toString(), 9 - countNum(newStateNode.getState(), 0));

    }

    private StateTreeNode generateState(StateTreeNode parent) {
        INDArray stateArray = Nd4j.zeros(3, 3); //new state
        Nd4j.copy(parent.getState(), stateArray);
        boolean insertedNewValue = false;
        int currentMove = ticOrToe.apply(countNum(stateArray, 1) > countNum(stateArray, 2));
        Integer[] possibleCols = IntStream.range(0, 3).flatMap(stream-> IntStream.range(0, 3)).boxed().toArray(Integer[]::new);
        Integer[] possibleRows = IntStream.range(0, 3).flatMap(stream-> IntStream.range(0, 3)).boxed().toArray(Integer[]::new);
        List<Integer> colsList = new ArrayList<>(Arrays.asList(possibleCols));
        List<Integer> rowsList = new ArrayList<>(Arrays.asList(possibleRows));
        while (!insertedNewValue) {
            int rowIndex = randomGenerator.nextInt(rowsList.size());
            int colIndex = randomGenerator.nextInt(colsList.size());
            int randomRow = rowsList.get(rowIndex);
            int randomCol = colsList.get(colIndex);
            rowsList.remove(rowIndex);
            colsList.remove(colIndex);
            log.info(Arrays.toString(colsList.toArray(new Integer[colsList.size()])));
            if(stateArray.getInt(randomRow, randomCol) == 0) {
                stateArray.put(randomRow, randomCol, currentMove);
                if(!parent.hasState(stateArray)) {
                    insertedNewValue = true;
                } else {
                    stateArray.put(randomRow, randomCol, 0);
                }
            }
        }

        return StateTreeNode.builder()
                .state(stateArray)
                .parent(parent)
                .build();
    }

    private int countNum(INDArray stateArray, int num) {
        int zeroSum = 0;
        for(int row = 0; row < stateArray.rows(); ++row) {
            for(int col = 0; col < stateArray.columns(); ++col) {
                zeroSum += stateArray.getInt(row, col) == num ? 1 : 0;
            }
        }
        return zeroSum;
    }

    /**
     * Rewind tree up to the root
     *
     * @param parent tree node
     * @return Root node of a tree
     */
    private StateTreeNode findRoot(StateTreeNode parent) {
        StateTreeNode root = parent;
        while(root.getParent() != null) {
            root = root.getParent();
        }
        return root;
    }
}
