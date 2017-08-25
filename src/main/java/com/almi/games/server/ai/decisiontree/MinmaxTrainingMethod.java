package com.almi.games.server.ai.decisiontree;

import com.almi.games.server.ai.LearningMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by Almi on 8/24/2017.
 */
@Slf4j
@Component
public class MinmaxTrainingMethod implements LearningMethod<StateTreeNode> {

    @Override
    public void train(StateTreeNode learningSet) {
//        log.info(learningSet.toString());
        log.info(learningSet.toString());
    }

}
