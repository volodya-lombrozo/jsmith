package com.github.lombrozo.jsmith.antlr.rules;

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
public final class Identifier extends UnimplementedRuleDefinition {
    public Identifier(final RuleDefinition parent) {
        super(parent);
    }
}
