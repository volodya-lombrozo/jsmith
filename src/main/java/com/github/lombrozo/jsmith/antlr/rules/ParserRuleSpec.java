package com.github.lombrozo.jsmith.antlr.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser spec rule.
 * The rule definition in ANTLR grammar:
 * {@code
 * parserRuleSpec
 *     : ruleModifiers? RULE_REF argActionBlock? ruleReturns? throwsSpec? localsSpec? rulePrequel* COLON {@link RuleBlock} SEMI
 *         exceptionGroup
 *     ;
 * }
 * @since 0.1
 */
public final class ParserRuleSpec implements RuleDefinition {

    private final RuleDefinition parent;

    private final String name;
    private List<RuleDefinition> list;

    public ParserRuleSpec(final String name, final RuleDefinition parent) {
        this.name = name;
        this.parent = parent;
        this.list = new ArrayList<>();
    }

    public String name() {
        return this.name;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.list.stream()
            .map(RuleDefinition::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.list.add(rule);
    }

    @Override
    public String toString() {
        return String.format("parserRuleSpec(%s)", this.name);
    }
}
