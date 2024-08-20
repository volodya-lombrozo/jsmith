package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.antlr.ProductionsChain;
import com.github.lombrozo.jsmith.antlr.RecursionException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class Unparser {

    private final Map<String, UnparserRule> rules;

    private final Map<String, UnlexerRule> unlexerRules;

    private final AtomicInteger stack = new AtomicInteger(0);

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

    public String generate(final String rule) {
        if (!this.rules.containsKey(rule)) {
            throw new IllegalStateException(
                String.format("Rule not found: %s. All available rules: [%s]", rule, this.rules)
            );
        }
        if (this.stack.incrementAndGet() > 50) {
            throw new RecursionException(
                String.format(
                    "Recursion detected in rule: %n%s%n",
                    new ProductionsChain(this.rules.get(rule)).toTree()
                )
            );
        }
        return this.rules.get(rule).generate();
    }
}
