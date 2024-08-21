package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//todo: remove this abstract class and replace inheritance with composition!
public abstract class UnimplementedGenerative implements Generative {
    private final Generative parent;
    private final List<Generative> children;

    protected UnimplementedGenerative(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    protected UnimplementedGenerative(final Generative parent, final List<Generative> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public String generate() {
        return this.children.stream().map(Generative::generate).collect(Collectors.joining(" "));
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public void append(final Generative generative) {
        this.children.add(generative);
    }
}
