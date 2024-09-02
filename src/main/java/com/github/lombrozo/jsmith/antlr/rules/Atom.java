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

/**
 * Atom.
 * The ANTLR grammar definition:
 * {@code
 * atom
 *     : {@link TerminalDef}
 *     | {@link Ruleref}
 *     | {@link NotSet}
 *     | DOT {@link ElementOptions}?
 *     ;
 * }
 * @since 0.1
 */
public final class Atom implements Rule {

    /**
     * Parent rule.
     */
    private final Rule parent;

    /**
     * Atom inner element.
     */
    private Rule item;

    /**
     * Constructor.
     * @param rule Parent rule.
     */
    public Atom(final Rule rule) {
        this.parent = rule;
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public void append(final Rule rule) {
        this.item = rule;
    }

    @Override
    public String name() {
        return "atom";
    }

    @Override
    public String generate(final Context context) {
        return this.item.generate(context);
    }
}
