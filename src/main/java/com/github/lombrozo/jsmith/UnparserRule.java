package com.github.lombrozo.jsmith;

public final class UnparserRule {


    private final String name;

    public UnparserRule(final String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public String generate() {
        return "";
    }
}
