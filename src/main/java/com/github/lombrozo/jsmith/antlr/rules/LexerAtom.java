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

import com.github.lombrozo.jsmith.random.Convergence;
import com.github.lombrozo.jsmith.antlr.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * LexerAtom rule.
 * The ANTLR grammar definition:
 * {@code
 * lexerAtom
 *     : {@link CharacterRange}
 *     | {@link TerminalDef}
 *     | {@link NotSet}
 *     | {@link LexerCharSet}
 *     | DOT {@link ElementOptions}?
 *     ;
 */
public final class LexerAtom implements Rule {

    /**
     * Parent rule.
     */
    private final Rule parent;

    /**
     * Children rules.
     */
    private final List<Rule> elems;

    /**
     * Constructor.
     */
    public LexerAtom() {
        this(new Root());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LexerAtom(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param convergence Convergence strategy.
     */
    public LexerAtom(final Rule parent, final Convergence<Rule> convergence) {
        this(parent, new ArrayList<>(0), convergence);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param elems Children rules.
     */
    private LexerAtom(final Rule parent, final List<Rule> elems) {
        this(parent, elems, new Convergence<>());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param elems Children rules.
     * @param convergence Convergence strategy.
     */
    public LexerAtom(
        final Rule parent,
        final List<Rule> elems,
        final Convergence<Rule> convergence
    ) {
        this.parent = parent;
        this.elems = elems;
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public String generate(final Context context) {
        final Convergence<Rule> convergence = context.strategy();
        return convergence.choose(this, this.elems)
            .generate(context.withConvergence(convergence));
    }

    @Override
    public void append(final Rule rule) {
        this.elems.add(rule);
    }

    @Override
    public String name() {
        return "lexerAtom";
    }

}
