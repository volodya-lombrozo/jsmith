package com.github.lombrozo.jsmith.antlr;

public final class CharacterRange implements Generative {

    private final Generative parent;
    private final String text;

    public CharacterRange(final Generative parent, final String text) {
        this.parent = parent;
        this.text = text;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.text;
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("CharacterRange cannot have children yet");
    }
}
