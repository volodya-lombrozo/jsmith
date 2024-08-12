package com.github.lombrozo.jsmith.antlr;

public final class Alternative implements Generative{

    private Element element;

    public void withElement(final Element element) {
        this.element = element;
    }

    @Override
    public String generate() {
        return element.generate();
    }
}
