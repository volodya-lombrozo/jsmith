package com.github.lombrozo.jsmith;

import java.util.HashMap;
import java.util.Map;

public final class Unparser {

    private final String top;
    private final Map<String, UnparserRule> rules;

    private final Map<String, UnlexerRule> unlexerRules;

    public Unparser(final String top) {
        this(top, new HashMap<>(0), new HashMap<>(0));
    }

    private Unparser(
        final String top,
        final Map<String, UnparserRule> rules,
        final Map<String, UnlexerRule> unlexerRules
    ) {
        this.top = top;
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

    public String generate() {
        return this.rules.get(this.top).generate();
    }

    public String generate(final String rule) {
        return this.rules.get(rule).generate();
    }
}
