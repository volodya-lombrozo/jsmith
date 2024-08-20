package com.github.lombrozo.jsmith.antlr;

/**
 * Alternative elements.
 * The ANTLR grammar definition:
 * {@code
 * element
 *     : labeledElement (ebnfSuffix |)
 *     | atom (ebnfSuffix |)
 *     | ebnf
 *     | actionBlock (QUESTION predicateOptions?)?
 *     ;
 * }
 * @since 0.1
 */
public final class Element implements Generative {

    private final Generative parent;
    private Generative atom;

    public Element(final Generative parent) {
        this.parent = parent;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.atom.generate();
    }

    @Override
    public void append(final Generative generative) {
        this.atom = generative;
    }

    @Override
    public String toString() {
        return "element";
    }
}
