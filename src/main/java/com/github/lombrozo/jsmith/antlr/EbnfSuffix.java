package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Randomizer;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
public final class EbnfSuffix implements Generative {

    private final Generative parent;
    private final String operation;
    private final String question;

    private final Randomizer randomizer = new Randomizer();

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

    public Generative multiplier(Generative from) {
        if (this.operation.equals("?")) {
            if (this.randomizer.flip()) {
                return from;
            } else {
                return new Empty();
            }
        } else if (this.operation.equals("+")) {
            int number = this.randomizer.nextInt(5) + 1;
            return new Several(Collections.nCopies(number, from));
        } else if (this.operation.equals("*")) {
            int number = this.randomizer.nextInt(5);
            return new Several(Collections.nCopies(number, from));
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "Unsupported operation %s for EbnfSuffix %s",
                    this.operation,
                    this
                )
            );
        }
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("Unsupported operation yet");
    }

    @Override
    public String toString() {
        return String.format(
            "ebnfSuffix(%s%s)", this.operation, Optional.ofNullable(this.question).orElse("")
        );
    }
}
