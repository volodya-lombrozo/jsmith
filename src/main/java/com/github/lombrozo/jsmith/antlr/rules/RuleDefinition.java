package com.github.lombrozo.jsmith.antlr.rules;

public interface RuleDefinition {

    RuleDefinition parent();

    String generate();

    void append(final RuleDefinition rule);

}
