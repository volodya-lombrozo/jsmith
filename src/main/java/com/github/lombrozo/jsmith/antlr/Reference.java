package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Unparser;

public final class Reference implements ElementItem {
    private final String ref;
    private final Unparser unparser;

    public Reference(final String ref, final Unparser unparser) {
        this.ref = ref;
        this.unparser = unparser;
    }

    @Override
    public String generate() {
        return this.unparser.generate(this.ref);
    }
}
