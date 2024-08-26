package com.github.lombrozo.jsmith.antlr.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Alternative elements.
 * The ANTLR grammar definition:
 * {@code
 * element
 *     : {@link LabeledElement} ({@link EbnfSuffix} |)
 *     | {@link Atom} ({@link EbnfSuffix} |)
 *     | {@link Ebnf}
 *     | {@link ActionBlock} (QUESTION predicateOptions?)?
 *     ;
 * }
 * @since 0.1
 */
public final class Element implements RuleDefinition {

    private final RuleDefinition parent;
    private List<RuleDefinition> children;

    public Element(final RuleDefinition parent) {
        this.parent = parent;
        this.children = new ArrayList<>(1);
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (this.children.isEmpty()) {
            throw new IllegalStateException("Element should have at least one child");
        }
        final RuleDefinition first = this.children.get(0);
        if (first instanceof Atom) {
            if (this.children.size() == 1) {
                return first.generate();
            } else {
                final EbnfSuffix ebnfSuffix = (EbnfSuffix) this.children.get(1);
                return ebnfSuffix.multiplier(first).generate();
            }
        } else if (first instanceof Ebnf) {
            return first.generate();
        } else {
            throw new IllegalStateException(
                String.format("Unrecognized element type '%s' for '%s' element", first, this)
            );
        }
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.children.add(rule);
    }

    @Override
    public String toString() {
        return "element";
    }
}
