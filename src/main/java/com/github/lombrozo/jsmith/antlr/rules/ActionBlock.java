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
 * ActionBlock rule.
 * The ANTLR grammar definition:
 * {@code
 * actionBlock
 *     : BEGIN_ACTION ACTION_CONTENT* END_ACTION
 *     ;
 * }
 *
 * @since 0.1
 */
public final class ActionBlock implements Rule {

    /**
     * Element name.
     */
    private static final String ALIAS = "actionBlock";

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Action content.
     */
    private final List<Rule> content;

    /**
     * Constructor.
     */
    public ActionBlock() {
        this(new Root());
    }

    /**
     * Constructor.
     *
     * @param parent Parent rule.
     */
    public ActionBlock(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param content Action content.
     */
    public ActionBlock(final Rule parent, final List<Rule> content) {
        this.top = parent;
        this.content = content;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        return new LeftToRight(this, this.content).generate(context);
    }

    @Override
    public void append(final Rule rule) {
        this.content.add(rule);
    }

    @Override
    public String name() {
        return ActionBlock.ALIAS;
    }

    @Override
    public Rule copy() {
        return new ActionBlock(
            this.parent(),
            this.content.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }

    static boolean isActionBlock(final Rule rule) {
        return ActionBlock.ALIAS.equals(rule.name());
    }
}
