/*
 * MIT License
 *
 * Copyright (c) 2023-2025 Volodya Lombrozo
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
import com.github.lombrozo.jsmith.antlr.view.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ElementOption rule.
 * The ANTLR grammar definition:
 * {@code
 * elementOption
 *     : {@link Identifier}
 *     | {@link Identifier} ASSIGN ({@link Identifier} | STRING_LITERAL)
 *     ;
 * }
 * @since 0.1
 */
public final class ElementOption implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * List of rules.
     */
    private final List<Rule> rules;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public ElementOption(final Rule parent) {
        this(parent, new ArrayList<Rule>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule
     * @param rules List of rules
     */
    public ElementOption(final Rule parent, final List<Rule> rules) {
        this.top = parent;
        this.rules = rules;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        if (this.rules.size() != 1 && this.rules.size() != 3) {
            throw new IllegalStateException(
                String.format(
                    "ElementOption should have 1 or 3 rules, provided: %d",
                    this.rules.size()
                )
            );
        }
        return new LeftToRight(this, this.rules).generate(context);
    }

    @Override
    public void append(final Rule rule) {
        this.rules.add(rule);
    }

    @Override
    public String name() {
        return "elementOption";
    }

    @Override
    public Rule copy() {
        return new ElementOption(
            this.parent(),
            this.rules.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }
}
