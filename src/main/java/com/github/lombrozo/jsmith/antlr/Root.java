package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Root implements Generative {

    private final List<Generative> all;

    public Root() {
        this(new ArrayList<>(0));
    }

    public Root(final List<Generative> all) {
        this.all = all;
    }

    @Override
    public Generative parent() {
        return this;
    }

    @Override
    public String generate() {
        return this.all.stream().map(Generative::generate).collect(Collectors.joining(" "));
    }

    @Override
    public void append(final Generative generative) {
        this.all.add(generative);
    }
}
