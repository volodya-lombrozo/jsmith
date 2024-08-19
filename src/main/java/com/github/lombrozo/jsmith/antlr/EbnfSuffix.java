package com.github.lombrozo.jsmith.antlr;

import java.util.Objects;
import java.util.Optional;
import lombok.ToString;

/**
 * Ebnf suffix ANTLR production.
 * The rule definition:
 * {@code
 * ebnfSuffix
 *     : QUESTION QUESTION?
 *     | STAR QUESTION?
 *     | PLUS QUESTION?
 *     ;
 * }
 * @since 0.1
 */
@ToString
public final class EbnfSuffix implements Generative {

    private final Generative parent;
    private final String operation;
    private final String question;

    public EbnfSuffix(final String operation) {
        this(new Generative.Empty(), operation);
    }

    EbnfSuffix(final String operation, final String question) {
        this(new Generative.Empty(), operation, question);
    }

    EbnfSuffix(final Generative parent, final String operation) {
        this(parent, operation, "");
    }

    EbnfSuffix(final Generative parent, final String operation, final String question) {
        this.parent = parent;
        this.operation = operation;
        this.question = question;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (Objects.isNull(this.operation)) {
            throw new IllegalArgumentException(
                String.format(
                    "Operation is required for EbnfSuffix %s",
                    this
                )
            );
        }
        return String.format(
            "%s%s",
            this.operation,
            Optional.ofNullable(this.question).orElse("")
        );
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("Unsupported operation yet");
    }
}
