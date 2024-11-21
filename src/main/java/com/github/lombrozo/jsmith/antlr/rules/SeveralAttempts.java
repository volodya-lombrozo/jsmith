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

import com.github.lombrozo.jsmith.antlr.view.Text;
import java.util.function.Supplier;

/**
 * Attempt to generate output.
 * @since 0.1
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
     * Original output generator.
     */
    private final Supplier<? extends Text> generator;

    /**
     * Constructor.
     * @param original Original output generator.
     */
    SeveralAttempts(final Supplier<? extends Text> original) {
        this(SeveralAttempts.DEFAULT_ATTEMPTS, original);
    }

    /**
     * Constructor.
     * @param attempts Maximum attempts to generate output.
     * @param original Original output generator.
     */
    private SeveralAttempts(final int attempts, final Supplier<? extends Text> original) {
        this.max = attempts;
        this.generator = original;
    }

    /**
     * Choose output.
     * @return Output.
     */
    public Text choose() {
        Text text = this.generator.get();
        int attempt = 0;
        while (text.error()) {
            text = this.generator.get();
            attempt++;
            if (attempt > this.max) {
                throw new IllegalStateException(
                    String.format(
                        "Can't generate output because constantly receive errors. I made %d attempts to generate output, but failed",
                        this.max
                    )
                );
            }
        }
        return text;
    }
}
