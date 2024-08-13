package com.github.lombrozo.jsmith.antlr;

import java.util.stream.Stream;

public interface Generative {

    Generative parent();

    String generate();

    void append(final Generative generative);

}
