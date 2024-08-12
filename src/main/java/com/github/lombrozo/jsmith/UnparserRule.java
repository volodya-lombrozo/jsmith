package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.antlr.AltList;
import com.github.lombrozo.jsmith.antlr.Generative;

public final class UnparserRule implements Generative {

    private final String name;
    private AltList list;


    public UnparserRule(final String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    @Override
    public String generate() {
        return list.generate();
    }

    public void append(final AltList altList) {
        this.list = altList;
    }
}
