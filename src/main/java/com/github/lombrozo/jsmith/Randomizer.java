package com.github.lombrozo.jsmith;

import java.util.Random;

public final class Randomizer {

    Random random = new Random();

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public boolean flip() {
        return this.random.nextBoolean();
    }

}
