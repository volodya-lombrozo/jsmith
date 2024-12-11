/*
 * MIT License
 *
 * Copyright (c) 2023-2024 Volodya Lombrozo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import com.github.lombrozo.jsmith.antlr.view.TextSnippet;
import com.github.lombrozo.jsmith.random.Multiplier;
import com.github.lombrozo.jsmith.random.Rand;
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
public final class EbnfSuffix implements Rule, Suffix {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Operation.
     */
    private final String operation;

    /**
     * Question.
     */
    private final String question;

    /**
     * Random generator.
     */
    private final Rand rand;

    /**
     * Constructor.
     * @param operation Operation.
     * @param random Random generator.
     */
    public EbnfSuffix(final String operation, final Rand random) {
        this(new Empty(), operation, "", random);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param operation Operation.
     * @param question Question.
     * @param rand Random generator.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    public EbnfSuffix(
        final Rule parent,
        final String operation,
        final String question,
        final Rand rand
    ) {
        this.top = parent;
        this.operation = operation;
        this.question = question;
        this.rand = rand;
    }

    /**
     * Constructor.
     * @param operation Operation.
     */
    EbnfSuffix(final String operation) {
        this(operation, "");
    }

    /**
     * Constructor.
     * @param operation Operation.
     * @param question Question.
     */
    EbnfSuffix(final String operation, final String question) {
        this(new Empty(), operation, question);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param operation Operation.
     * @param question Question.
     */
    private EbnfSuffix(final Rule parent, final String operation, final String question) {
        this(parent, operation, question, new Rand());
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Snippet generate(final Context context) {
        if (Objects.isNull(this.operation)) {
            throw new IllegalArgumentException(
                String.format(
                    "Operation is required for EbnfSuffix %s",
                    this
                )
            );
        }
        return new TextSnippet(
            this,
            String.format(
                "%s%s",
                this.operation,
                Optional.ofNullable(this.question).orElse("")
            )
        );
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("Unsupported operation yet");
    }

    @Override
    public String name() {
        return String.format(
            "ebnfSuffix(%s%s)", this.operation, Optional.ofNullable(this.question).orElse("")
        );
    }

    @Override
    public Rule copy() {
        return new EbnfSuffix(this.parent(), this.operation, this.question, this.rand);
    }

    @Override
    public Multiplier multiplier() {
        final Multiplier result;
        switch (this.operation) {
            case "?":
                result = new Multiplier.ZeroOrOne(this.rand);
                break;
            case "+":
                result = new Multiplier.OneOrMore(this.rand);
                break;
            case "*":
                result = new Multiplier.ZeroOrMore(this.rand);
                break;
            default:
                throw new IllegalArgumentException(
                    String.format(
                        "Unsupported operation %s for EbnfSuffix %s",
                        this.operation,
                        this
                    )
                );
        }
        return result;
    }
}
