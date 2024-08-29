package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Convergence;
import com.github.lombrozo.jsmith.Rand;
import com.github.lombrozo.jsmith.antlr.rules.RuleDefinition;

public final class GenerationContext {
    private final Rand rand;
    private final Convergence<RuleDefinition> convergence;

    public GenerationContext() {
        this(new Rand(), new Convergence<>());
    }

    public GenerationContext(
        final Rand rand,
        final Convergence<RuleDefinition> convergence
    ) {
        this.rand = rand;
        this.convergence = convergence;
    }

    public Rand rand() {
        return this.rand;
    }

    public Convergence<RuleDefinition> convergence() {
        return this.convergence.copy();
    }

    public GenerationContext withConvergence(final Convergence<RuleDefinition> convergence) {
        return new GenerationContext(this.rand, convergence);
    }

}
