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
import com.github.lombrozo.jsmith.random.Multiplier;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a lexer element.
 * lexerElement
 *     : {@link LexerAtom} {@link EbnfSuffix}?
 *     | {@link LexerBlock} {@link EbnfSuffix}?
 *     | {@link ActionBlock} QUESTION?
 *     ;
 */
public final class LexerElement implements Rule {

    /**
     * Parent rule.
     */
    private final Rule parent;

    /**
     * Children rules.
     */
    private final List<Rule> children;

    /**
     * Constructor.
     */
    public LexerElement() {
        this(new Root());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LexerElement(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    private LexerElement(final Rule parent, final List<Rule> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public String generate(final Context context) {
        return this.multiplier().repeat(this.children.get(0)).generate(context);
    }

    @Override
    public void append(final Rule rule) {
        this.children.add(rule);
    }

    @Override
    public String name() {
        return "lexerElement";
    }

    /**
     * Returns the multiplier for the element.
     * @return The multiplier for the {@link LexerAtom}, {@link ActionBlock} and {@link LexerBlock}.
     */
    private Multiplier multiplier() {
        final Multiplier result;
        if (this.children.size() == 1) {
            result = new Multiplier.One();
        } else {
            result = ((EbnfSuffix) this.children.get(1)).multiplier();
        }
        return result;
    }
}
