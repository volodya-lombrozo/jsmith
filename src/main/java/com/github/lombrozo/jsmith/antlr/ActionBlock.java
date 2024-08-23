package com.github.lombrozo.jsmith.antlr;

/**
 * ActionBlock rule.
 * The ANTLR grammar definition:
 * {@code
 * actionBlock
 *     : BEGIN_ACTION ACTION_CONTENT* END_ACTION
 *     ;
 * }
 * @since 0.1
 */
public final class ActionBlock extends UnimplementedRuleDefinition {
    public ActionBlock(final RuleDefinition parent) {
        super(parent);
    }
}
