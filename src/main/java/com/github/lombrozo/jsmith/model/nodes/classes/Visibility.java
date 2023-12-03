package com.github.lombrozo.jsmith.model.nodes.classes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Visibility {
    PUBLIC,
    PROTECTED,
    PRIVATE;

    private static final List<Visibility> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Visibility getRandomVisibility() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
