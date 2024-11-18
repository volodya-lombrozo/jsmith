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
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.random.Multiplier;

/**
 * Traced rule.
 * This rule isn't ANTLR rule, it's a wrapper for the rule to trace it during generation process.
 * In other words, it's a decorator for a real rule.
 * @since 0.1
 */
public final class Traced implements Rule, Suffix, Negatable {

    /**
     * Real rule.
     */
    private final Rule original;

    /**
     * Constructor.
     * @param rule Real rule.
     */
    public Traced(final Rule rule) {
        this.original = rule;
    }

    @Override
    public Rule parent() {
        return this.original.parent();
    }

    @Override
    public Text generate(final Context context) {
        return this.original.generate(context.next(this.original));
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
        return new Traced(this.original.copy());
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
    public Text negate(final Context context) {
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
