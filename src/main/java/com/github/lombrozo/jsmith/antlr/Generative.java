package com.github.lombrozo.jsmith.antlr;

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

        @Override
        public String toString() {
            return "empty";
        }
    }

}
