package com.github.lombrozo.jsmith.antlr;

/**
 * LabeledAlt rule.
 * The ANTLR grammar definition:
 * {@code
 * labeledAlt
 *     : {@link Alternative} (POUND {@link Identifier})?
 *     ;
 * }
 */
public final class LabeledAlt extends UnimplementedGenerative {
    public LabeledAlt(final Generative parent) {
        super(parent);
    }

    @Override
    public String toString() {
        return "labeledAlt";
    }
}
