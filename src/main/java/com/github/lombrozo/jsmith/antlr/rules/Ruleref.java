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

import com.github.lombrozo.jsmith.antlr.GenerationContext;
import com.github.lombrozo.jsmith.antlr.RecursionException;
import com.github.lombrozo.jsmith.antlr.representation.ProductionsChain;
import com.github.lombrozo.jsmith.antlr.Unparser;

/**
 * Parser rule reference.
 * The rule definition in ANTLR grammar:
 * {@code
 * ruleref
 *     : RULE_REF {@link ArgActionBlock}? {@link ElementOptions}?
 *     ;
 * }
 * @since 0.1
 * @todo #1:30min Add more test cases for rule ref.
 *  Currently we handle only the case when the rule reference is a simple string.
 *  We need to add more test cases to cover the cases with argActionBlock and elementOptions.
 */
public final class Ruleref implements Rule {

    /**
     * Parent rule.
     */
    private final Rule parent;

    /**
     * Rule reference.
     */
    private final String ref;

    /**
     * Unparser.
     */
    private final Unparser unparser;

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param ref Rule reference.
     * @param unparser Unparser.
     */
    public Ruleref(
        final Rule parent,
        final String ref,
        final Unparser unparser
    ) {
        this.parent = parent;
        this.ref = ref;
        this.unparser = unparser;
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public String generate(final GenerationContext context) {
        try {
            return this.unparser.generate(this.ref, context);
        } catch (final RecursionException exception) {
            throw new IllegalStateException(
                String.format(
                    "Recursion detected in rule reference %s",
                    new ProductionsChain(this).tree()
                ),
                exception
            );

        }
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("Reference cannot have children yet");
    }

    @Override
    public String toString() {
        return String.format("ruleref(%s)", this.ref);
    }
}
