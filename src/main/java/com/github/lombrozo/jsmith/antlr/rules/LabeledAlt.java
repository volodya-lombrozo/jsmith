package com.github.lombrozo.jsmith.antlr.rules;

/**
 * LabeledAlt rule.
 * The ANTLR grammar definition:
 * {@code
 * labeledAlt
 *     : {@link Alternative} (POUND {@link Identifier})?
 *     ;
 * }
 * @since 0.1
 */
public final class LabeledAlt extends Unimplemented {

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LabeledAlt(final Rule parent) {
        super(parent);
    }

    @Override
    public String name() {
        return String.format("labeledAlt(id=%d)", System.identityHashCode(this));
    }
}

