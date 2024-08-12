package com.github.lombrozo.jsmith.antlr;

public final class Terminal implements ElementItem {

    private final String text;

    public Terminal(final String text) {
        this.text = text;
    }

    @Override
    public String generate() {
        return this.text;
    }
}
