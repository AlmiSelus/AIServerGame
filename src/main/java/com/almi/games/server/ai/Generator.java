package com.almi.games.server.ai;

/**
 * Created by Almi on 8/24/2017.
 */
public interface Generator<T> {
    T generate();
    T generate(T startState);
}
