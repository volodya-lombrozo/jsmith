package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.antlr.AltList;
import com.github.lombrozo.jsmith.antlr.Generative;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class UnlexerRule implements Generative {

    private final Generative parent;

    private final String name;
    private List<Generative> list;

    public UnlexerRule(final Generative parent, final String name) {
        this.parent = parent;
        this.name = name;
        this.list = new ArrayList<>(0);
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
        return this.list.stream().map(Generative::generate).collect(Collectors.joining(" "));
    }

    @Override
    public void append(final Generative generative) {
        this.list.add(generative);
    }
}
