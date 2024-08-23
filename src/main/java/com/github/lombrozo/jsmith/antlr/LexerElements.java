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
public final class LexerElements implements RuleDefinition {

    private final RuleDefinition parent;

    private final List<RuleDefinition> elems;

    public LexerElements(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    public LexerElements(final RuleDefinition parent, final List<RuleDefinition> elems) {
        this.parent = parent;
        this.elems = elems;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.elems.stream().map(RuleDefinition::generate).collect(Collectors.joining(""));
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.elems.add(rule);
    }

    @Override
    public String toString() {
        return "lexerElements";
    }
}
