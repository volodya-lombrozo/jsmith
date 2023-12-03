package com.github.lombrozo.jsmith.utils;

import java.util.Random;

public class RandomGen {

    private static final Random random = new Random();

    public static int getNextInt(int bound) {
        return random.nextInt(bound);
    }
}
