package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.antlr.AltList;
import com.github.lombrozo.jsmith.antlr.Generative;

public final class UnlexerRule implements Generative {

    private final Generative parent;

    private final String name;
    private AltList list;

    public UnlexerRule(final Generative parent, final String name) {
        this.parent = parent;
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public void append(final AltList altList) {
        this.list = altList;
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

    }
}
