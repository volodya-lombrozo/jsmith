package com.github.lombrozo.jsmith.antlr.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Root implements RuleDefinition {

    private final List<RuleDefinition> all;

    public Root() {
        this(new ArrayList<>(0));
    }

    public Root(final List<RuleDefinition> all) {
        this.all = all;
    }

    @Override
    public RuleDefinition parent() {
        return this;
    }

    @Override
    public String generate() {
        return this.all.stream().map(RuleDefinition::generate).collect(Collectors.joining(" "));
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.all.add(rule);
    }

    @Override
    public String toString() {
        return "root";
    }
}
