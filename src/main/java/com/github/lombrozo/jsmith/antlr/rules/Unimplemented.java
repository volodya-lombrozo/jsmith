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

/**
 * Unimplemented rule.
 * This rule was created to speed up the development process.
 * All the rules that are not implemented yet should inherit from this class.
 * Later, when some rules are implemented, this class should be removed.
 * @since 0.1
 * @todo #1:90min Implement all the rules that inherit from Unimplemented class.
 *  The Unimplemented class was created to speed up the development process.
 *  All the rules that implement this class should be implemented.
 *  When all the rules are implemented, this class should be removed.
 */
public abstract class Unimplemented implements Rule {

    /**
     * Parent rule.
     */
    private final Rule parentr;

    /**
     * Children rules.
     */
    private final List<Rule> children;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    protected Unimplemented(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    protected Unimplemented(final Rule parent, final List<Rule> children) {
        this.parentr = parent;
        this.children = children;
    }

    @Override
    public final Node generate(final Context context) throws WrongPathException {
        return new LeftToRight(this, this.children).generate(context);
    }

    @Override
    public final Rule parent() {
        return this.parentr;
    }

    @Override
    public final void append(final Rule rule) {
        this.children.add(rule);
    }
}
