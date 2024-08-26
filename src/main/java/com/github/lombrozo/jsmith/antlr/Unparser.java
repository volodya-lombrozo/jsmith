package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.antlr.rules.LexerRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.ParserRuleSpec;
import com.github.lombrozo.jsmith.antlr.representation.ProductionsChain;
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

    private final Map<String, LexerRuleSpec> unlexerRules;

    private final AtomicInteger stack = new AtomicInteger(0);

    public Unparser() {
        this(new HashMap<>(0), new HashMap<>(0));
    }

    private Unparser(
        final Map<String, ParserRuleSpec> rules,
        final Map<String, LexerRuleSpec> unlexerRules
    ) {
        this.rules = rules;
        this.unlexerRules = unlexerRules;
    }

    public Unparser withParserRule(final ParserRuleSpec rule) {
        this.rules.put(rule.name(), rule);
        return this;
    }

    public Unparser withLexerRule(final LexerRuleSpec rule) {
        this.unlexerRules.put(rule.name(), rule);
        return this;
    }

    public LexerRuleSpec unlexerRule(final String rule) {
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
                    new ProductionsChain(this.rules.get(rule)).tree()
                )
            );
        }
        final String generate = this.rules.get(rule).generate();
        this.stack.decrementAndGet();
        return generate;
    }
}
