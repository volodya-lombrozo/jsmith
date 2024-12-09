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
import com.github.lombrozo.jsmith.antlr.LeafSnippet;
import com.github.lombrozo.jsmith.antlr.NodeSnippet;
import com.github.lombrozo.jsmith.antlr.Snippet;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextNode;
import com.github.lombrozo.jsmith.random.Multiplier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ebnf rule.
 * The ANTLR grammar definition:
 * {@code
 * ebnf
 *     : {@link Block} {@link BlockSuffix}?
 *     ;
 * }
 * @since 0.1
 */
public final class Ebnf implements Rule {

    /**
     * This rule name.
     */
    private static final String ALIAS = "ebnf";

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Block and suffix.
     */
    private final List<Rule> children;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public Ebnf(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     */
    Ebnf() {
        this(new Root());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    private Ebnf(final Rule parent, final List<Rule> children) {
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
                "Ebnf should have at least one 'Block', but it's empty"
            );
        }
        return new NodeSnippet(
            this, this.multiplier().repeat(this.children.get(0)).generate(context)
        );
    }

    @Override
    public void append(final Rule rule) {
        this.children.add(rule);
    }

    @Override
    public String name() {
        return Ebnf.ALIAS;
    }

    @Override
    public Rule copy() {
        return new Ebnf(
            this.top, this.children.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }

    /**
     * Check if the rule is Ebnf.
     * @param rule Rule.
     * @return True if the rule is Ebnf.
     */
    static boolean isEbnf(final Rule rule) {
        return Ebnf.ALIAS.equals(rule.name());
    }

    /**
     * Get multiplier.
     * @return Multiplier.
     */
    private Multiplier multiplier() {
        final Multiplier result;
        if (this.children.size() <= 1) {
            result = new Multiplier.One();
        } else {
            final Rule first = this.children.get(1);
            if (first instanceof Suffix) {
                result = ((Suffix) first).multiplier();
            } else {
                throw new IllegalStateException(
                    String.format(
                        "Unknown block suffix type: '%s' and name '%s'",
                        first.getClass().getSimpleName(),
                        first.name()
                    )
                );
            }
        }
        return result;
    }
}
