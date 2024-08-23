package com.github.lombrozo.jsmith.antlr;

/**
 * PredicateOptions rule.
 * The ANTLR grammar definition:
 * {@code
 * predicateOptions
 *     : LT predicateOption (COMMA predicateOption)* GT
 *     ;
 * }
 * @since 0.1
 */
public final class PredicateOptions extends UnimplementedRuleDefinition {
    public PredicateOptions(final RuleDefinition parent) {
        super(parent);
    }
}
