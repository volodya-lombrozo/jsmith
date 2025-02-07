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
 * Element options.
 * The ANTLR grammar definition:
 * {@code
 * elementOptions
 *     : LT {@link ElementOption} (COMMA {@link ElementOption})* GT
 *     ;
 * }
 * @since 0.1
 */
public final class ElementOptions implements Rule {
    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * List of child rules.
     */
    private final List<Rule> rules;

    /**
     * Constructor.
     * @param parent Parent rule
     */
    public ElementOptions(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule
     * @param rules List of rules.
     */
    public ElementOptions(final Rule parent, final List<Rule> rules) {
        this.top = parent;
        this.rules = rules;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        return new LeftToRight(this, this.rules).generate(context);
    }

    @Override
    public void append(final Rule rule) {
        this.rules.add(rule);
    }

    @Override
    public String name() {
        return "elementOptions";
    }

    @Override
    public Rule copy() {
        return new ElementOptions(
            this.parent(),
            this.rules.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }
}
