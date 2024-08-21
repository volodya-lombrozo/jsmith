package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Block rule.
 * The ANTLR grammar definition:
 * {@code
 * block
 *     : LPAREN (optionsSpec? ruleAction* COLON)? {@link AltList} RPAREN
 *     ;
 * }
 * @since 0.1
 */
public final class Block implements Generative {

    private final Generative parent;
    private final List<Generative> children;

    public Block(final Generative parent) {
        this(parent, new ArrayList<>());
    }

    public Block(final Generative parent, final List<Generative> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.children.stream()
            .map(Generative::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final Generative generative) {
        this.children.add(generative);
    }
}
