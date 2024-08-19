package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents lexer elements.
 * ANTLR grammar:
 * {@code
 * lexerElements
 *     : lexerElement+
 *     |
 *     ;
 * }
 */
public final class LexerElements implements Generative {

    private final Generative parent;

    private final List<Generative> elems;

    public LexerElements(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    public LexerElements(final Generative parent, final List<Generative> elems) {
        this.parent = parent;
        this.elems = elems;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.elems.stream().map(Generative::generate).collect(Collectors.joining(""));
    }

    @Override
    public void append(final Generative generative) {
        this.elems.add(generative);
    }

    @Override
    public String toString() {
        return "lexerElements";
    }
}
