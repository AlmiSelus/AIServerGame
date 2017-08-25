package com.almi.games.server.ai;

/**
 * Created by Almi on 8/24/2017.
 */
public interface LearningMethod<T> {
    void train(T learningSet);
}
