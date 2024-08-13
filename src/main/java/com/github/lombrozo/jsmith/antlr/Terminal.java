package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.UnlexerRule;
import com.github.lombrozo.jsmith.Unparser;

public final class Terminal implements ElementItem {

    private final Generative parent;

    private final Unparser unparser;
    private final String text;

    public Terminal(final Generative parent, final Unparser unparser, final String text) {
        this.parent = parent;
        this.unparser = unparser;
        this.text = text;
    }


    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        final UnlexerRule rule = this.unparser.unlexerRule(this.text);
        //todo: fix null check - remove it!
        if (rule == null) {
            return this.text;
        }
        return rule.generate();
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("Terminal cannot have children yet");
    }
}
