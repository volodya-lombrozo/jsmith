package com.github.lombrozo.jsmith.antlr;

public final class LexerCharSet implements Generative {
    private final Generative parent;
    private final String text;

    public LexerCharSet(final Generative parent, final String text) {
        this.parent = parent;
        this.text = text;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.text;
    }

    public char[] alphabet() {
        final String substring = text.substring(1, text.length() - 1);
        final char first = substring.charAt(0);
        final char last = substring.charAt(substring.length() - 1);
        final int length = last - first;
        char[] alphabet = new char[length];
        for (char index = first; index < last; ++index) {
            alphabet[index - first] = index;
        }
        return alphabet;
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("LexerCharSet cannot have children yet");
    }
}
