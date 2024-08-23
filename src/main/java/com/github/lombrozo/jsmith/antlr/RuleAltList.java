package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Rand;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule alternative list.
 * The ruleAltList definition in ANTLR grammar:
 * {@code
 * ruleAltList
 *     : labeledAlt (OR labeledAlt)*
 *     ;
 * }
 * @since 0.1
 */
public final class RuleAltList implements RuleDefinition {

    private final RuleDefinition parent;
    private final List<RuleDefinition> alternatives;

    private final Rand rand = new Rand();

    public RuleAltList(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    public RuleAltList(final RuleDefinition parent, final List<RuleDefinition> alternatives) {
        this.parent = parent;
        this.alternatives = alternatives;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (this.alternatives.isEmpty()) {
            throw new IllegalStateException("RuleAltList should have at least one alternative");
        }
        return this.alternatives.get(this.rand.nextInt(this.alternatives.size())).generate();
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.alternatives.add(rule);
    }

    @Override
    public String toString() {
        return String.format("ruleAltList(alternatives=%d)", this.alternatives.size());
    }
}
