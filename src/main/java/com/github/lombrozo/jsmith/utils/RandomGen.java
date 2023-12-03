package com.github.lombrozo.jsmith.utils;

import java.util.Random;

public class RandomGen {

    private static Random rand = new Random();

    public static int getNextInt(int bound) {
        return rand.nextInt(bound);
    }
}
