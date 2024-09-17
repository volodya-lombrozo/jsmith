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
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextLeaf;
import com.github.lombrozo.jsmith.random.Rand;

/**
 * LexerCharSet rule.
 * @since 0.1
 */
public final class LexerCharSet implements Rule {

    /**
     * Parent rule.
     */
    private final Rule parent;


    /**
     * Random generator.
     */
    private final Rand rand;

    /**
     * Char set.
     * Might be a range or a set of characters.
     */
    private final String text;

    /**
     * Constructor.
     * @param text Char set that might be a range or a set of characters.
     */
    public LexerCharSet(final String text) {
        this(new Root(), text);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param text Text.
     */
    public LexerCharSet(final Rule parent, final String text) {
        this(parent, new Rand(), text);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param rand Random generator.
     * @param text Text.
     */
    public LexerCharSet(final Rule parent, final Rand rand, final String text) {
        this.parent = parent;
        this.rand = rand;
        this.text = text;
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public Text generate(final Context context) {
        return new TextLeaf(
            this,
            this.rand.regex(this.text)
        );
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("LexerCharSet cannot have children");
    }

    @Override
    public String name() {
        return "lexerCharSet(not-a-rule)";
    }
}
