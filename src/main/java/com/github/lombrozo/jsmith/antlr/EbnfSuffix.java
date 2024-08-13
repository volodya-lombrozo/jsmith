package com.github.lombrozo.jsmith.antlr;

public final class EbnfSuffix implements Generative {
    private final Generative parent;
    private final String operation;

    //todo?
    private String question;

    public EbnfSuffix(final Generative parent) {
        this.parent = parent;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.operation;
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("Unsupported operation yet");
    }
}
