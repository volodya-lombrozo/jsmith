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

/**
 * Traced rule.
 * This rule isn't ANTLR rule, it's a wrapper for the rule to trace it during generation process.
 * In other words, it's a decorator for a real rule.
 * @since 0.1
 */
public final class Traced implements Rule {

    /**
     * Real rule.
     */
    private final Rule rule;

    /**
     * Constructor.
     * @param rule Real rule.
     */
    public Traced(final Rule rule) {
        this.rule = rule;
    }

    @Override
    public Rule parent() {
        return this.rule.parent();
    }

    @Override
    public String generate(final Context context) {
        return this.rule.generate(context.next(this.rule));
    }

    @Override
    public void append(final Rule rule) {
        this.rule.append(rule);
    }

    @Override
    public String name() {
        return this.rule.name();
    }
}
