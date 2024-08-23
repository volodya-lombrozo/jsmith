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
public final class Block implements RuleDefinition {

    private final RuleDefinition parent;
    private final List<RuleDefinition> children;

    public Block(final RuleDefinition parent) {
        this(parent, new ArrayList<>());
    }

    public Block(final RuleDefinition parent, final List<RuleDefinition> children) {
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
}
