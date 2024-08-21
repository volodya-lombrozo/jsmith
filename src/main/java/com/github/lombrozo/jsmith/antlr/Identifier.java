package com.github.lombrozo.jsmith.antlr;

/**
 * Identifier rule.
 * The ANTLR grammar definition:
 * {@code
 * identifier
 *     : RULE_REF
 *     | TOKEN_REF
 *     ;
 * }
 * @since 0.1
 */
public final class Identifier extends UnimplementedGenerative {
    public Identifier(final Generative parent) {
        super(parent);
    }
}
