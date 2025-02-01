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
import com.github.lombrozo.jsmith.antlr.view.TerminalNode;
import com.github.lombrozo.jsmith.random.Rand;

/**
 * Literal rule.
 * ANTLR makes no distinction between a single character literal and a
 * multi-character string. All literals are single quote delimited and
 * may contain unicode escape sequences of the form \\uxxxx, where x
 * is a valid hexadecimal number (per Unicode standard).
 * The ANTLR grammar definition:
 * WARNING: This is NOT a part of the ANTLR grammar!
 * @since 0.1
 */
public final class Literal implements Rule, Negatable {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Text of the literal.
     */
    private final String text;

    /**
     * Random generator.
     */
    private final Rand random;

    /**
     * Constructor.
     * @param text Text of the literal.
     */
    public Literal(final String text) {
        this(new Root(), text);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param text Text of the literal.
     */
    public Literal(final Rule parent, final String text) {
        this(parent, text, new Rand());
    }

    /**
     * Constructor.
     * @param top Parent rule.
     * @param text Text of the literal.
     * @param random Random generator.
     */
    public Literal(final Rule top, final String text, final Rand random) {
        this.top = top;
        this.text = text;
        this.random = random;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) {
        return new TerminalNode(
            this,
            new AntlrString(this.text).asString()
        );
    }

    @Override
    public Node negate(final Context context) {
        return new TerminalNode(
            this,
            this.random.regex(String.format("[^%s]", this.generate(context).text().output()))
        );
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("Literal cannot have children yet");
    }

    @Override
    public String name() {
        return String.format("literal(%s)", this.text);
    }

    @Override
    public Rule copy() {
        return new Literal(this.top, this.text);
    }

    @Override
    public String toString() {
        return this.name();
    }
}
