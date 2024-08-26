package com.github.lombrozo.jsmith.antlr.rules;

public final class CharacterRange implements RuleDefinition {

    private final RuleDefinition parent;
    private final String text;

    public CharacterRange(final RuleDefinition parent, final String text) {
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
        throw new UnsupportedOperationException("CharacterRange cannot have children yet");
    }
}
