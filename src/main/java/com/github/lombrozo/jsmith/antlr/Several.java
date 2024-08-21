package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Several implements Generative {

    private final List<Generative> all;

    public Several() {
        this(new ArrayList<>(0));
    }

    public Several(final Generative... all) {
        this(Arrays.asList(all));
    }

    public Several(final List<Generative> all) {
        this.all = all;
    }

    @Override
    public Generative parent() {
        throw new UnsupportedOperationException("This node can't implement this");
    }

    @Override
    public String generate() {
        return this.all.stream()
            .map(Generative::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("This node can't implement this");
    }
}
