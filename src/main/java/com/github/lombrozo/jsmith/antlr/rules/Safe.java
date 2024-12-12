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
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import com.github.lombrozo.jsmith.antlr.view.Trace;
import com.github.lombrozo.jsmith.random.Multiplier;
import java.util.Collections;
import java.util.List;

/**
 * Safe rule.
 * This is NOT an ANTLR grammar rule!
 * Rule that ensures that the recursion is not detected.
 * @since 0.1
 */
public final class Safe implements Rule, Suffix, Negatable {

    /**
     * Default max recursion depth.
     */
    static final int DEFAULT = 600;

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
    public Snippet generate(final Context context) {
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
    public List<Rule> children(final Context context) {
        final List<Rule> path = context.path();
        if (path.size() >= this.limit) {
            throw new RecursionException(
                String.format(
                    "Long generation path! Most probably you have a recursion here: %s",
                    new Trace(path).line()
                )
            );
        }
        return Collections.singletonList(this.original);
    }

    @Override
    public void append(final Rule rule) {
        this.original.append(rule);
    }

    @Override
    public String name() {
        return this.original.name();
    }

    @Override
    public Rule copy() {
        return new Safe(this.original.copy(), this.limit);
    }

    @Override
    public Multiplier multiplier() {
        if (this.original instanceof Suffix) {
            return ((Suffix) this.original).multiplier();
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "Rule '%s' with name '%s' doesn't support multiplier",
                    this.original.getClass().getSimpleName(),
                    this.original.name()
                )
            );
        }
    }

    @Override
    public Snippet negate(final Context context) {
        if (this.original instanceof Negatable) {
            return ((Negatable) this.original).negate(context);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "Rule '%s' with name '%s' doesn't support negation",
                    this.original.getClass().getSimpleName(),
                    this.original.name()
                )
            );
        }
    }
}
