package com.almi.games.server.ai.decisiontree;

import com.almi.games.server.ai.Generator;
import io.vavr.Tuple2;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.random.RandomGenerator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Created by Almi on 8/24/2017.
 */
@Slf4j
@Component
public class StateTreeGenerator implements Generator<StateTreeNode> {

    private static final int MAX_LEVEL_TO_GENERATE = 9;

    @Autowired
    private RandomGenerator randomGenerator;

    @Setter
    private int levelToGenerate = 0;
    private Function<Boolean, Integer> ticOrToe = (i)-> i ? 2 : 1;

    @Getter
    private int counter = 0;

    @Override
    public StateTreeNode generate() {
        return generate(StateTreeNode.builder().build());
    }

    @Override
    public StateTreeNode generate(StateTreeNode startState) {
        generateTree(startState, 0, levelToGenerate != 0 ? levelToGenerate : MAX_LEVEL_TO_GENERATE);
        return findRoot(startState);
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

        StateTreeNode newStateNode = generateState(root);
        root.getChildren().add(newStateNode);
        log(newStateNode, currentLevel);

        counter++;
        for(int j = 0; j < countNum(newStateNode.getState(), 0); ++j) {
            generateTree(newStateNode, currentLevel+1, maxLevel);
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
        PossibleIndicesMatrix possibleValuesMatrix = PossibleIndicesMatrix.of(3, 3);
        int index = 0;
        while (!insertedNewValue) {
            index++;
            int indicesIndex = randomGenerator.nextInt(possibleValuesMatrix.size());
            Tuple2<Integer, Integer> indices = possibleValuesMatrix.getTuple(indicesIndex);
            possibleValuesMatrix.remove(indicesIndex);
            if(stateArray.getInt(indices._1(), indices._2()) != 0) {
                continue;
            }

            stateArray.put(indices._1(), indices._2(), currentMove);

            if(!parent.hasState(stateArray)) {
                insertedNewValue = true;
            } else {
                stateArray.put(indices._1(), indices._2(), 0);
            }

        }

        log.info("Found state after {} moves", index);

        return StateTreeNode.builder().state(stateArray).parent(parent).build();
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
