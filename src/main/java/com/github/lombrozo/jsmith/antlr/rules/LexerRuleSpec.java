package com.github.lombrozo.jsmith.antlr.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lexer rule specification.
 * The ANTLR rule definition:
 * {@code
 * lexerRuleSpec
 *     : FRAGMENT? TOKEN_REF optionsSpec? COLON {@link LexerRuleBlock} SEMI
 *     ;
 * }
 * @since 0.1
 */
public final class LexerRuleSpec implements RuleDefinition {

    private final RuleDefinition parent;

    private final String name;
    private List<RuleDefinition> list;

    public LexerRuleSpec(final RuleDefinition parent, final String name) {
        this.parent = parent;
        this.name = name;
        this.list = new ArrayList<>(0);
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
        return this.list.stream().map(RuleDefinition::generate).collect(Collectors.joining(" "));
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.list.add(rule);
    }
}
