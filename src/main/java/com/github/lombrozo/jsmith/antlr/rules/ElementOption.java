package com.github.lombrozo.jsmith.antlr.rules;

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
public final class ElementOption extends UnimplementedRuleDefinition {
    public ElementOption(final RuleDefinition parent) {
        super(parent);
    }
}
