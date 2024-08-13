package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.antlr.Generative;

public final class UnparserRule implements Generative {

    private final Generative parent;

    private final String name;
    private Generative list;

    public UnparserRule(final String name, final Generative parent) {
        this.name = name;
        this.parent = parent;
    }

    public String name() {
        return this.name;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.list.generate();
    }

    @Override
    public void append(final Generative generative) {
        this.list = generative;
    }

}
