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
import com.github.lombrozo.jsmith.antlr.RecursionException;
import com.github.lombrozo.jsmith.antlr.view.Trace;
import java.util.List;

/**
 * Safe rule.
 * This is NOT an ANTLR grammar rule!
 * Rule that ensures that the recursion is not detected.
 * @since 0.1
 */
public final class Safe implements Rule {

    /**
     * Default max recursion depth.
     */
    private static final int DEFAULT = 200;

    /**
     * Original rule.
     * All the calls are delegated to this rule.
     */
    private final Rule original;

    /**
     * Max allowed recursion depth.
     */
    private final int limit;

    /**
     * Constructor.
     */
    public Safe() {
        this(new Root());
    }

    /**
     * Constructor.
     * @param original Original rule.
     */
    public Safe(final Rule original) {
        this(original, Safe.DEFAULT);
    }

    /**
     * Constructor.
     * @param original Original rule.
     * @param limit Max allowed recursion depth.
     */
    public Safe(final Rule original, final int limit) {
        this.original = original;
        this.limit = limit;
    }

    @Override
    public Rule parent() {
        return this.original.parent();
    }

    @Override
    public String generate(final Context context) {
        final List<Rule> path = context.path();
        if (path.size() >= this.limit) {
            throw new RecursionException(
                String.format(
                    "Long generation path! Most probably you have a recursion here: %s",
                    new Trace(path).line()
                )
            );
        }
        return this.original.generate(context);
    }

    @Override
    public void append(final Rule rule) {
        this.original.append(rule);
    }

    @Override
    public String name() {
        return this.original.name();
    }
}
