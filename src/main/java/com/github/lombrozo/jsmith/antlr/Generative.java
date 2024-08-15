package com.github.lombrozo.jsmith.antlr;

import java.util.stream.Stream;

public interface Generative {

    Generative parent();

    String generate();

    void append(final Generative generative);

    class Empty implements Generative {

        @Override
        public Generative parent() {
            return this;
        }

        @Override
        public String generate() {
            return "";
        }

        @Override
        public void append(final Generative ignore) {
        }
    }

}
