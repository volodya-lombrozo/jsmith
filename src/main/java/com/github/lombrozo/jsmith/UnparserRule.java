package com.github.lombrozo.jsmith;

public final class UnparserRule {

    private final String name;
    private AltList list;


    public UnparserRule(final String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public String generate() {
        return "";
    }

    public void append(final AltList altList) {
        this.list = altList;
    }
}
