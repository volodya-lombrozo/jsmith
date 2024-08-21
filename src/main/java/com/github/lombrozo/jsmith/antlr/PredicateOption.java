package com.github.lombrozo.jsmith.antlr;

/**
 * PredicateOption rule.
 * The ANTLR grammar definition:
 * {@code
 * predicateOption
 *     : {@link ElementOption}
 *     | {@link Identifier} ASSIGN {@link ActionBlock}
 *     ;
 * }
 * @since 0.1
 */
public final class PredicateOption extends UnimplementedGenerative {
    public PredicateOption(final Generative parent) {
        super(parent);
    }
}
