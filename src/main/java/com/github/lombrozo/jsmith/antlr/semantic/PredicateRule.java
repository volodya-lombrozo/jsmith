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
package com.github.lombrozo.jsmith.antlr.semantic;

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.rules.WrongPathException;
import com.github.lombrozo.jsmith.antlr.view.Node;
import java.util.Optional;

/**
 * Rule that adds type to the context.
 * @since 0.1
 */
public final class PredicateRule implements Rule {

    /**
     * Comment to activate this rule.
     */
    public static final String COMMENT = "$jsmith-predicate";

    /**
     * Original rule.
     */
    private final Rule origin;

    /**
     * Type of the predicate.
     */
    private final String type;

    /**
     * Constructor.
     * @param origin Original rule.
     * @param type Type of the predicate.
     */
    public PredicateRule(final Rule origin, final String type) {
        this.origin = origin;
        this.type = type;
    }

    @Override
    public Rule parent() {
        return this.origin.parent();
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        final Node res;
        final Optional<String> opttype = context.attributes().currentType();
        if (opttype.isPresent()) {
            final String current = opttype.get();
            if (current.equals(this.type)) {
                res = this.origin.generate(context);
            } else {
                throw new WrongPathException(
                    String.format("Type mismatch, expected: %s, but got: %s", this.type, current)
                );
            }
        } else {
            res = this.origin.generate(context);
        }
        return res;
    }

    @Override
    public void append(final Rule rule) {
        this.origin.append(rule);
    }

    @Override
    public String name() {
        return this.origin.name();
    }

    @Override
    public Rule copy() {
        return new PredicateRule(this.origin.copy(), this.type);
    }
}
