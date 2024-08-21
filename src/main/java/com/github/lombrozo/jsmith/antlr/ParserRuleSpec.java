package com.github.lombrozo.jsmith.antlr;

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
public final class ParserRuleSpec implements Generative {

    private final Generative parent;

    private final String name;
    private List<Generative> list;

    public ParserRuleSpec(final String name, final Generative parent) {
        this.name = name;
        this.parent = parent;
        this.list = new ArrayList<>();
    }

    public String name() {
        return this.name;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.list.stream()
            .map(Generative::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final Generative generative) {
        this.list.add(generative);
    }

    @Override
    public String toString() {
        return String.format("parserRuleSpec(%s)", this.name);
    }
}
