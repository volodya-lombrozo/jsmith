package com.github.lombrozo.jsmith.antlr;

public final class Atom implements Generative {
    private ElementItem item;

    public void with(final ElementItem reference) {
        this.item = reference;
    }

    public String generate() {
        return this.item.generate();
    }
}
