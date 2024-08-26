package com.github.lombrozo.jsmith.antlr.rules;

/**
 * LabeledAlt rule.
 * The ANTLR grammar definition:
 * {@code
 * labeledAlt
 *     : {@link Alternative} (POUND {@link Identifier})?
 *     ;
 * }
 */
public final class LabeledAlt extends Unimplemented {
    public LabeledAlt(final RuleDefinition parent) {
        super(parent);
    }

    @Override
    public String toString() {
        return "labeledAlt";
    }
}
