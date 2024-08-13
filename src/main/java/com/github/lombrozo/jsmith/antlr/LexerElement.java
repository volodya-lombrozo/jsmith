package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class LexerElement implements Generative {

    private final Generative parent;

    private final List<Generative> children;

    public LexerElement(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    public LexerElement(final Generative parent, final List<Generative> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.children.stream().map(Generative::generate).collect(Collectors.joining(" "));
    }

    @Override
    public void append(final Generative generative) {
        this.children.add(generative);
    }
}
