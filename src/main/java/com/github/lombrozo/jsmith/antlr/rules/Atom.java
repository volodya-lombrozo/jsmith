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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
     * Atom name.
     */
    private static final String ALIAS = "atom";

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Atom inner element.
     */
    private final AtomicReference<Rule> item;

    /**
     * Constructor.
     * @param rule Parent rule.
     */
    public Atom(final Rule rule) {
        this(rule, new AtomicReference<>());
    }

    private Atom(final Rule top, final AtomicReference<Rule> item) {
        this.top = top;
        this.item = item;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public void append(final Rule rule) {
        this.item.set(rule);
    }

    @Override
    public String name() {
        return Atom.ALIAS;
    }

    @Override
    public Rule copy() {
        return new Atom(this.top, this.item);
    }

    @Override
    public Snippet generate(final Context context) {
        return new SignedSnippet(this, this.item.get().generate(context));
    }

    @Override
    public List<Rule> children(final Context context) {
        return Collections.singletonList(this.item.get());
    }

    /**
     * Check if the rule is an atom.
     * @param rule Rule.
     * @return True if the rule is an atom.
     */
    static boolean isAtom(final Rule rule) {
        return Atom.ALIAS.equals(rule.name());
    }
}
