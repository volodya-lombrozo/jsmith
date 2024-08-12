package com.github.lombrozo.jsmith;

import java.util.HashMap;
import java.util.Map;

public final class Unparser {

    private final String top;
    private final Map<String, UnparserRule> rules;

    public Unparser(final String top) {
        this(top, new HashMap<>(0));
    }

    public Unparser(final String top, final Map<String, UnparserRule> rules) {
        this.top = top;
        this.rules = rules;
    }

    public Unparser withRule(final UnparserRule rule) {
        this.rules.put(rule.name(), rule);
        return this;
    }

    public String generate() {
        return this.rules.get(this.top).generate();
    }
}
