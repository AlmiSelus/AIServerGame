package com.almi.games.server.ai.decisiontree;

import com.almi.games.server.ai.Generator;
import io.vavr.Tuple2;
import lombok.Getter;
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

    @Autowired
    private RandomGenerator randomGenerator;

    private Function<Boolean, Integer> ticOrToe = (i)-> i ? 2 : 1;

    @Getter
    private int counter = 0;

    @Override
    public StateTreeNode generate() {
        return generate(StateTreeNode.builder().build());
    }

    @Override
    public StateTreeNode generate(StateTreeNode startState) {
        int currentPlacesToFill = countNum(startState.getState(), 0);
        generateTree(startState, currentPlacesToFill);
        log.info("Generated {} nodes", counter);
        return startState;
    }

    /**
     * 1. Count how much states need to be generated
     * 2. Generate new state
     * 3. Add state as children to current state node
     * 4. Repeat for all states until given depth found
     */
    private void generateTree(StateTreeNode root, int currentPlacesToFill) {
        counter++;
        for(int i = 0; i < currentPlacesToFill; ++i) {
            StateTreeNode newStateNode = generateState(root);
            root.getChildren().add(newStateNode);
            generateTree(newStateNode, currentPlacesToFill-1);
        }
    }

    private StateTreeNode generateState(StateTreeNode parent) {
        INDArray stateArray = Nd4j.zeros(3, 3); //new state
        Nd4j.copy(parent.getState(), stateArray);
        boolean insertedNewValue = false;
        int currentMove = ticOrToe.apply(countNum(stateArray, 1) > countNum(stateArray, 2));
        PossibleIndicesMatrix possibleValuesMatrix = PossibleIndicesMatrix.of(parent, 0);
        do {
            int indicesIndex = randomGenerator.nextInt(possibleValuesMatrix.size());
            if(possibleValuesMatrix.wasVisited(indicesIndex)) {
                continue;
            }

            possibleValuesMatrix.visit(indicesIndex);
            Tuple2<Integer, Integer> indices = possibleValuesMatrix.getTuple(indicesIndex);
            stateArray = stateArray.put(indices._1(), indices._2(), currentMove);
            if(!parent.hasState(stateArray)) {
                insertedNewValue = true;
            } else {
                stateArray.put(indices._1(), indices._2(), 0);
            }
        } while(!insertedNewValue);

        return StateTreeNode.builder().state(stateArray).build();
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
}
