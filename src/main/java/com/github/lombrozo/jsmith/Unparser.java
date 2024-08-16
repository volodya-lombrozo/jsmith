package com.github.lombrozo.jsmith;

import java.util.HashMap;
import java.util.Map;

public final class Unparser {

    private final Map<String, UnparserRule> rules;

    private final Map<String, UnlexerRule> unlexerRules;

    public Unparser() {
        this(new HashMap<>(0), new HashMap<>(0));
    }

    private Unparser(
        final Map<String, UnparserRule> rules,
        final Map<String, UnlexerRule> unlexerRules
    ) {
        this.rules = rules;
        this.unlexerRules = unlexerRules;
    }

    public Unparser withParserRule(final UnparserRule rule) {
        this.rules.put(rule.name(), rule);
        return this;
    }

    public Unparser withLexerRule(final UnlexerRule rule) {
        this.unlexerRules.put(rule.name(), rule);
        return this;
    }
    public UnlexerRule unlexerRule(final String rule) {
        return this.unlexerRules.get(rule);
    }

    //todo: trace generation path?
    public String generate(final String rule) {
        return this.rules.get(rule).generate();
    }
}
