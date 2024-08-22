package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.antlr.ParserRuleSpec;
import com.github.lombrozo.jsmith.antlr.ProductionsChain;
import com.github.lombrozo.jsmith.antlr.RecursionException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @todo #1:90min Unify Recursion Detection.
 *   We use a stack to detect recursion in the {@link Unparser} class.
 *   And we use a chain of productions to detect recursion in the
 *   {@link com.github.lombrozo.jsmith.antlr.ANTLRListener}.
 *   Maybe we should unify these two approaches.
 */
public final class Unparser {

    private final Map<String, ParserRuleSpec> rules;

    private final Map<String, UnlexerRule> unlexerRules;

    private final AtomicInteger stack = new AtomicInteger(0);

    public Unparser() {
        this(new HashMap<>(0), new HashMap<>(0));
    }

    private Unparser(
        final Map<String, ParserRuleSpec> rules,
        final Map<String, UnlexerRule> unlexerRules
    ) {
        this.rules = rules;
        this.unlexerRules = unlexerRules;
    }

    public Unparser withParserRule(final ParserRuleSpec rule) {
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
        //todo: to high stack! We neeed to learn how to control it!
        if (this.stack.incrementAndGet() > 500) {
            throw new RecursionException(
                String.format(
                    "Recursion detected in rule: %n%s%n",
                    new ProductionsChain(this.rules.get(rule)).toTree()
                )
            );
        }
        final String generate = this.rules.get(rule).generate();
        this.stack.decrementAndGet();
        return generate;
    }
}
