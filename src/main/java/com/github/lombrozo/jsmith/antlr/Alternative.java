package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alternative ANTLR rule.
 * The rule definition:
 * {@code
 * alternative
 *     : elementOptions? {@link Element}+
 *     |
 *     // explicitly allow empty alts
 *     ;
 * }
 */
public final class Alternative implements RuleDefinition {

    private final RuleDefinition parent;
    private final List<RuleDefinition> elements;

    Alternative(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    private Alternative(
        final RuleDefinition parent,
        final List<RuleDefinition> element
    ) {
        this.parent = parent;
        this.elements = element;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.elements.stream()
            .map(RuleDefinition::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.elements.add(rule);
    }

    @Override
    public String toString() {
        return "alternative";
    }
}
