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
import com.github.lombrozo.jsmith.antlr.view.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Action rule.
 * The ANTLR grammar definition:
 * {@code
 * action_
 *     : AT ({@link ActionScopeName} COLONCOLON)? {@link Identifier} {@link ActionBlock}
 *     ;
 * }
 * @since 0.1
 */
public final class Action implements Rule {
    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Elements list.
     */
    private final List<Rule> elements;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public Action(final Rule parent) {
        this(parent, new ArrayList<Rule>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param elements ActionScopeName, Identifier and ActionBlock.
     */
    public Action(final Rule parent, final List<Rule> elements) {
        this.top = parent;
        this.elements = elements;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        if (this.elements.size() < 3) {
            throw new IllegalStateException(
                "Action must have at least 3 elements: at, identifier and actionBlock"
            );
        } else if (this.elements.size() > 5) {
            throw new IllegalStateException(
                "Action must have at most 5 elements: At, actionScopeName, COLONCOLON, identifier and actionBlock"
            );
        }
        return new LeftToRight(this, this.elements).generate(context);
    }

    @Override
    public void append(final Rule rule) {
        this.elements.add(rule);
    }

    @Override
    public String name() {
        return "action_";
    }

    @Override
    public Rule copy() {
        return new Action(
            this.parent(), this.elements.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }
}
