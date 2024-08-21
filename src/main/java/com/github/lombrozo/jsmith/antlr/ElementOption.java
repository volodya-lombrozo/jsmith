package com.github.lombrozo.jsmith.antlr;

/**
 * ElementOption rule.
 * The ANTLR grammar definition:
 * {@code
 * elementOption
 *     : {@link Identifier}
 *     | {@link Identifier} ASSIGN ({@link Identifier} | STRING_LITERAL)
 *     ;
 * }
 * @since 0.1
 */
public final class ElementOption extends UnimplementedGenerative {
    public ElementOption(final Generative parent) {
        super(parent);
    }
}
