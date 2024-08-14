package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Generator;
import com.mifmif.common.regex.Generex;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a lexer element.
 * lexerElement
 *     : lexerAtom ebnfSuffix?
 *     | lexerBlock ebnfSuffix?
 *     | actionBlock QUESTION?
 *     ;
 */
public final class LexerElement implements Generative {

    private final Generative parent;

    private final List<Generative> children;

    public LexerElement(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    public LexerElement(final Generative parent, final List<Generative> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (this.children.get(0) instanceof LexerAtom) {
            final String regex;
            final Generative atom = this.children.get(0);
            if (this.children.size() > 1) {
                final Generative ebnSuffix = this.children.get(1);
                if (ebnSuffix != null) {
                    if (ebnSuffix instanceof EbnfSuffix) {
                        regex = atom.generate() + ebnSuffix.generate();
                    } else {
                        throw new IllegalStateException(
                            String.format(
                                "The second element should be EbnfSuffix! But was %s, %s",
                                ebnSuffix.getClass().getSimpleName(),
                                ebnSuffix
                            )
                        );
                    }
                } else {
                    regex = atom.generate();
                }
            } else {
                regex = atom.generate();
            }
            return LexerElement.fromRegex(regex);
        } else {
            return this.children.stream().map(Generative::generate)
                .collect(Collectors.joining(" "));
        }
    }

    private static String fromRegex(final String regex) {
        return new Generex(regex).random();
    }

    @Override
    public void append(final Generative generative) {
        this.children.add(generative);
    }
}
