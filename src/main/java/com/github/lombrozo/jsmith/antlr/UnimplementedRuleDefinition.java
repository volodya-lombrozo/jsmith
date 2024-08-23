package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//todo: remove this abstract class and replace inheritance with composition!
public abstract class UnimplementedRuleDefinition implements RuleDefinition {
    private final RuleDefinition parent;
    private final List<RuleDefinition> children;

    protected UnimplementedRuleDefinition(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    protected UnimplementedRuleDefinition(final RuleDefinition parent, final List<RuleDefinition> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public String generate() {
        return this.children.stream().map(RuleDefinition::generate).collect(Collectors.joining(" "));
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.children.add(rule);
    }
}
