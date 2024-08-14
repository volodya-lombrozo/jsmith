package com.github.lombrozo.jsmith.antlr;

public final class EbnfSuffix implements Generative {
    private final Generative parent;
    private final String operation;
    private final String question;

    public EbnfSuffix(final Generative parent, final String operation) {
        this(parent, operation, "");
    }

    public EbnfSuffix(final Generative parent, final String operation, final String question) {
        this.parent = parent;
        this.operation = operation;
        this.question = question;
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
