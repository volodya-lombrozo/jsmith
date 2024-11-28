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

import com.github.lombrozo.jsmith.antlr.view.Error;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.jcabi.log.Logger;
import java.util.function.Supplier;

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
    private final Supplier<? extends Text> generator;

    /**
     * Constructor.
     * @param author Author of the rule.
     * @param generator Original output generator.
     */
    public SeveralAttempts(
        final String author,
        final Supplier<? extends Text> generator
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
        final Supplier<? extends Text> original
    ) {
        this.max = attempts;
        this.author = author;
        this.generator = original;
    }

    /**
     * Choose output.
     * @return Output.
     * @todo #92:30min The loop in SeveralAttempts.choose() method is wrong.
     *  Take a look on the last generation attempt. It might be successful,
     *  but we don't return it. We need to fix this issue.
     *  Don't forget to add tests for this case.
     */
    public Text choose() {
        Text text = this.generator.get();
        int attempt = 1;
        while (text.error()) {
            if (attempt > this.max) {
                final String msg = String.format(
                    "Can't generate output because constantly receive errors. I made %d attempts to generate output, but failed, the rule is '%s:%s', Message '%s'",
                    this.max,
                    this.author,
                    text.writer().name(),
                    text.output()
                );
                Logger.warn(this, msg);
                text = new Error(text.writer(), msg);
                break;
            }
            text = this.generator.get();
            attempt = attempt + 1;
        }
        return text;
    }
}
