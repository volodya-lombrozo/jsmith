package com.github.lombrozo.jsmith.antlr;

public final class LexerCharSet implements Generative {
    private final Generative parent;
    private final String text;


    public LexerCharSet(final Generative parent, final String text) {
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
        throw new UnsupportedOperationException("LexerCharSet cannot have children yet");
    }
}
