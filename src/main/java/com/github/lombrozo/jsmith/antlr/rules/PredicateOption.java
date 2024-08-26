package com.github.lombrozo.jsmith.antlr.rules;

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
public final class PredicateOption extends UnimplementedRuleDefinition {
    public PredicateOption(final RuleDefinition parent) {
        super(parent);
    }
}
