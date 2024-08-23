package com.github.lombrozo.jsmith.antlr;

/**
 * Atom.
 * The ANTLR grammar definition:
 * {@code
 * atom
 *     : {@link TerminalDef}
 *     | {@link Ruleref}
 *     | notSet
 *     | DOT elementOptions?
 *     ;
 * }
 */
public final class Atom implements RuleDefinition {

    private final RuleDefinition parent;
    private RuleDefinition item;

    public Atom(final RuleDefinition parent) {
        this.parent = parent;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.item = rule;
    }

    @Override
    public String generate() {
        return this.item.generate();
    }

    @Override
    public String toString() {
        return "atom";
    }
}
