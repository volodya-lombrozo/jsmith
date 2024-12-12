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

import com.github.lombrozo.jsmith.antlr.Attributes;
import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.rules.WrongPathException;
import com.github.lombrozo.jsmith.antlr.view.ErrorSnippet;
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import com.jcabi.log.Logger;
import java.util.Collections;
import java.util.List;
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

    public PredicateRule(final Rule origin, final String type) {
        this.origin = origin;
        this.type = type;
    }

    @Override
    public Rule parent() {
        return this.origin.parent();
    }

    @Override
    public Snippet generate(final Context context) {
        final Snippet res;
        final Attributes attributes = context.attributes();
        final Optional<String> opttype = attributes.currentType();
        if (opttype.isPresent()) {
            final String current = opttype.get();
            if (current.equals(this.type)) {
                res = this.origin.generate(context);
            } else {
                final String msg = String.format(
                    "Type mismatch, expected: %s, but got: %s", this.type, current
                );
                Logger.warn(this, msg);
                res = new ErrorSnippet(this, msg);
            }
        } else {
            res = this.origin.generate(context);
        }
        return res;
    }

    @Override
    public List<Rule> children(final Context context) throws WrongPathException {
        final Rule res;
        final Attributes attributes = context.attributes();
        final Optional<String> opttype = attributes.currentType();
        if (opttype.isPresent()) {
            final String current = opttype.get();
            if (current.equals(this.type)) {
                res = this.origin;
            } else {
                throw new WrongPathException(String.format(
                    "Type mismatch, expected: %s, but got: %s", this.type, current
                )
                );
            }
        } else {
            res = this.origin;
        }
        return Collections.singletonList(res);
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
