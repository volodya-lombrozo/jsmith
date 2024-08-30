package com.github.lombrozo.jsmith.random;

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.List;

public final class ConvergenceStrategy implements ChoosingStrategy {
    private final Convergence<Rule> rule;

    public ConvergenceStrategy() {
        this(new Convergence<>());
    }

    public ConvergenceStrategy(final Convergence<Rule> rule) {
        this.rule = rule;
    }

    @Override
    public Rule choose(final Rule from, final List<Rule> elements) {
        return this.rule.choose(from, elements);
    }
}
