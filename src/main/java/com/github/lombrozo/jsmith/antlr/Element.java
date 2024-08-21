package com.github.lombrozo.jsmith.antlr;

import java.util.ArrayList;
import java.util.List;

/**
 * Alternative elements.
 * The ANTLR grammar definition:
 * {@code
 * element
 *     : {@link LabeledElement} ({@link EbnfSuffix} |)
 *     | {@link Atom} ({@link EbnfSuffix} |)
 *     | {@link Ebnf}
 *     | {@link ActionBlock} (QUESTION predicateOptions?)?
 *     ;
 * }
 * @since 0.1
 */
public final class Element implements Generative {

    private final Generative parent;
    private List<Generative> children;

    public Element(final Generative parent) {
        this.parent = parent;
        this.children = new ArrayList<>(1);
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (this.children.isEmpty()) {
            throw new IllegalStateException("Element should have at least one child");
        }
        final Generative first = this.children.get(0);
        if (first instanceof Atom) {
            if (this.children.size() == 1) {
                return first.generate();
            } else {
                final EbnfSuffix ebnfSuffix = (EbnfSuffix) this.children.get(1);
                return ebnfSuffix.multiplier(first).generate();
            }
        } else {
            throw new IllegalStateException(
                String.format("Unrecognized element type %s for element %s", first, this)
            );
        }
    }

    @Override
    public void append(final Generative generative) {
        this.children.add(generative);
    }

    @Override
    public String toString() {
        return "element";
    }
}
