package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Unparser;

public final class Reference implements ElementItem {

    private final Generative parent;
    private final String ref;
    private final Unparser unparser;

    public Reference(
        final Generative parent,
        final String ref,
        final Unparser unparser
    ) {
        this.parent = parent;
        this.ref = ref;
        this.unparser = unparser;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.unparser.generate(this.ref);
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("Reference cannot have children yet");
    }
}
