package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.Rand;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

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
public final class EbnfSuffix implements RuleDefinition {

    private final RuleDefinition parent;
    private final String operation;
    private final String question;

    private final Rand rand = new Rand();

    public EbnfSuffix(final String operation) {
        this(new Empty(), operation);
    }

    EbnfSuffix(final String operation, final String question) {
        this(new Empty(), operation, question);
    }

    public EbnfSuffix(final RuleDefinition parent, final String operation) {
        this(parent, operation, "");
    }

    public EbnfSuffix(final RuleDefinition parent, final String operation, final String question) {
        this.parent = parent;
        this.operation = operation;
        this.question = question;
    }

    @Override
    public RuleDefinition parent() {
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

    public RuleDefinition multiplier(RuleDefinition from) {
        if (this.operation.equals("?")) {
            if (this.rand.flip()) {
                return from;
            } else {
                return new Empty();
            }
        } else if (this.operation.equals("+")) {
            int number = this.rand.nextInt(5) + 1;
            return new Several(Collections.nCopies(number, from));
        } else if (this.operation.equals("*")) {
            int number = this.rand.nextInt(5);
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
    public void append(final RuleDefinition rule) {
        throw new UnsupportedOperationException("Unsupported operation yet");
    }

    @Override
    public String toString() {
        return String.format(
            "ebnfSuffix(%s%s)", this.operation, Optional.ofNullable(this.question).orElse("")
        );
    }
}
