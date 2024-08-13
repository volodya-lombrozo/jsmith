package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Alternative implements Generative {

    private final Generative parent;
    private final List<Generative> elements;

    public Alternative(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    public Alternative(
        final Generative parent,
        final List<Generative> element
    ) {
        this.parent = parent;
        this.elements = element;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.elements.stream()
            .map(Generative::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final Generative generative) {
        this.elements.add(generative);
    }
}
