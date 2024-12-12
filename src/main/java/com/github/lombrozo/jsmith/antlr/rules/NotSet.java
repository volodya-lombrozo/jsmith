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
import com.github.lombrozo.jsmith.antlr.view.SignedSnippet;
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * NotSet rule.
 * Inverts children's rules.
 * The ANTLR grammar definition:
 * {@code
 * notSet
 *     : NOT {@link SetElement}
 *     | NOT {@link BlockSet}
 *     ;
 * }
 * @since 0.1
 */
public final class NotSet implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Children rules.
     */
    private final List<Rule> children;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public NotSet(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    public NotSet(final Rule parent, final List<Rule> children) {
        this.top = parent;
        this.children = children;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Snippet generate(final Context context) {
        if (this.children.isEmpty()) {
            throw new IllegalStateException(
                "NotSet rule is empty, either SetElement or BlockSet should be added before generation"
            );
        }
        final Rule rule = this.children.get(0);
        if (rule instanceof Negatable) {
            return new SignedSnippet(this, ((Negatable) rule).negate(context));
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "Rule '%s' with name '%s' doesn't support negation",
                    rule.getClass().getSimpleName(),
                    rule.name()
                )
            );
        }
    }

    @Override
    public List<Rule> children(final Context context) {
        return Collections.emptyList();
    }

    @Override
    public void append(final Rule rule) {
        this.children.add(rule);
    }

    @Override
    public String name() {
        return "notSet";
    }

    @Override
    public Rule copy() {
        return new NotSet(
            this.top,
            this.children.stream().map(Rule::copy).collect(java.util.stream.Collectors.toList())
        );
    }
}
