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
import com.github.lombrozo.jsmith.antlr.Unparser;
import com.github.lombrozo.jsmith.antlr.view.Text;

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
 *  Currently, we handle only the case when the rule reference is a simple string.
 *  We need to add more test cases to cover the cases with argActionBlock and elementOptions.
 */
public final class Ruleref implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

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
        this.top = parent;
        this.ref = ref;
        this.unparser = unparser;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Text generate(final Context context) {
        return this.unparser.generate(this.ref, context);
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("Reference cannot have children yet");
    }

    @Override
    public String name() {
        return String.format("ruleref(%s)", this.ref);
    }

    @Override
    public Rule copy() {
        return new Ruleref(
            this.top,
            this.ref,
            this.unparser
        );
    }
}
