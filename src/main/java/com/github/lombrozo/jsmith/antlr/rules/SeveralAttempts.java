/*
 * MIT License
 *
 * Copyright (c) 2023-2025 Volodya Lombrozo
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

import com.github.lombrozo.jsmith.antlr.view.Node;
import com.jcabi.log.Logger;

/**
 * Attempt to generate output.
 * @since 0.1
 * @todo #92:90min Inefficient implementation of the SeveralAttempts class.
 *  We might spend a lot of time trying to generate output. We need to
 *  implement a more efficient way to generate output.
 *  For example, if the following production is incorrect:
 *  {@code
 *  assignment
 *     : /* $jsmith-var-target / leftHandSide'='simplifiedExpression
 *  ;
 *  }
 *  It will try all the 10 times. And we need tests for this class!
 */
public final class SeveralAttempts {

    /**
     * How many attempts to generate output by default.
     */
    private static final int DEFAULT_ATTEMPTS = 10;

    /**
     * Maximum attempts to generate output.
     */
    private final int max;

    /**
     * The author that makes attempts.
     */
    private final String author;

    /**
     * Original output generator.
     */
    private final Attempt generator;

    /**
     * Constructor.
     * @param author Author of the rule.
     * @param generator Original output generator.
     */
    public SeveralAttempts(
        final String author,
        final Attempt generator
    ) {
        this(SeveralAttempts.DEFAULT_ATTEMPTS, author, generator);
    }

    /**
     * Constructor.
     * @param attempts Maximum attempts to generate output.
     * @param author Author of the rule.
     * @param original Original output generator.
     */
    SeveralAttempts(
        final int attempts,
        final String author,
        final Attempt original
    ) {
        this.max = attempts;
        this.author = author;
        this.generator = original;
    }

    /**
     * Choose output.
     * @return Output.
     */
    public Node choose() throws WrongPathException {
        Node snippet = null;
        int attempt = 0;
        WrongPathException origin = null;
        do {
            try {
                snippet = this.generator.make();
            } catch (final WrongPathException exception) {
                Logger.warn(this, exception.getMessage());
                origin = exception;
            }
            attempt = attempt + 1;
        } while (snippet == null && attempt < this.max);
        if (snippet == null) {
            throw new WrongPathException(
                String.format(
                    "Can't generate output because constantly receive errors. I made %d attempts to generate output, but failed. The rule is '%s'",
                    this.max,
                    this.author
                ),
                origin
            );
        }
        return snippet;
    }

    /**
     * Attempt to generate output.
     * @since 0.1
     */
    @FunctionalInterface
    interface Attempt {
        Node make() throws WrongPathException;
    }
}
