package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Several implements RuleDefinition {

    private final List<RuleDefinition> all;

    public Several() {
        this(new ArrayList<>(0));
    }

    public Several(final RuleDefinition... all) {
        this(Arrays.asList(all));
    }

    public Several(final List<RuleDefinition> all) {
        this.all = all;
    }

    @Override
    public RuleDefinition parent() {
        throw new UnsupportedOperationException("This node can't implement this");
    }

    @Override
    public String generate() {
        return this.all.stream()
            .map(RuleDefinition::generate)
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final RuleDefinition rule) {
        throw new UnsupportedOperationException("This node can't implement this");
    }
}
