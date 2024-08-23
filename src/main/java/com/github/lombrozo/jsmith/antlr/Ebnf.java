package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ebnf rule.
 * The ANTLR grammar definition:
 * {@code
 * ebnf
 *     : {@link Block} {@link BlockSuffix}?
 *     ;
 * }
 */
public final class Ebnf implements RuleDefinition {

    private final RuleDefinition parent;

    private final List<RuleDefinition> children;

    public Ebnf(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    public Ebnf(final RuleDefinition parent, final List<RuleDefinition> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.children.stream()
            .map(RuleDefinition::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.children.add(rule);
    }

    @Override
    public String toString() {
        return "ebnf";
    }
}
