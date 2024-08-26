package com.github.lombrozo.jsmith.antlr.rules;

public final class Literal implements RuleDefinition {

    private final RuleDefinition parent;
    private final String text;

    public Literal(final String text) {
        this(new Empty(), text);
    }

    public Literal(final RuleDefinition parent, final String text) {
        this.parent = parent;
        this.text = text;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.text;
    }

    @Override
    public void append(final RuleDefinition rule) {
        throw new UnsupportedOperationException("Literal cannot have children yet");
    }
}
