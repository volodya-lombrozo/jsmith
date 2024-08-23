package com.github.lombrozo.jsmith.antlr;

/**
 * Rule block.
 * The ruleBlock definition in ANTLR grammar:
 * {@code
 * ruleBlock
 *     : {@link RuleAltList}
 *     ;
 * }
 * @since 0.1
 */
public final class RuleBlock extends UnimplementedRuleDefinition {
    public RuleBlock(final RuleDefinition parent) {
        super(parent);
    }

    @Override
    public String generate() {
        return super.generate();
    }

    @Override
    public String toString() {
        return "ruleBlock";
    }
}
