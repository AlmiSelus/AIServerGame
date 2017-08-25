package com.almi.games.server;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Random;

/**
 * Created by c309044 on 2017-08-02.
 */
@SpringBootApplication
public class AIServerGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(AIServerGameApplication.class, args);
    }

    @Bean
    public RandomGenerator randomGenerator() {
        return RandomGeneratorFactory.createRandomGenerator(new Random(new Date().getTime()));
    }

}
