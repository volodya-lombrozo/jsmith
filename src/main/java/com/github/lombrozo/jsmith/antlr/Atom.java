package com.github.lombrozo.jsmith.antlr;

public final class Atom implements Generative {

    private final Generative parent;
    private Generative item;

    public Atom(final Generative parent) {
        this.parent = parent;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public void append(final Generative generative) {
        this.item = generative;
    }

    @Override
    public String generate() {
        return this.item.generate();
    }
}
