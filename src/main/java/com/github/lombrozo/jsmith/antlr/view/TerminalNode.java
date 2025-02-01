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
package com.github.lombrozo.jsmith.antlr.view;

import com.github.lombrozo.jsmith.antlr.Attributes;
import com.github.lombrozo.jsmith.antlr.rules.Rule;

/**
 * Leaf snippet that does not contain any other snippets.
 * @since 0.1
 */
public final class TerminalNode implements Node {

    /**
     * Text.
     */
    private final Text txt;

    /**
     * Attributes.
     */
    private final Attributes attrs;

    /**
     * Constructor.
     * @param author Rule that produces the text.
     * @param text Text.
     */
    public TerminalNode(final Rule author, final String text) {
        this(new PlainText(author.name(), text));
    }

    /**
     * Constructor.
     * @param author Who writes the text.
     * @param text Text.
     */
    public TerminalNode(final String author, final String text) {
        this(new PlainText(author, text));
    }

    /**
     * Constructor.
     * @param text Text.
     */
    public TerminalNode(final Text text) {
        this(text, new Attributes());
    }

    /**
     * Constructor.
     * @param author Who writes the text.
     * @param text Text.
     * @param attributes Attributes.
     */
    public TerminalNode(final String author, final String text, final Attributes attributes) {
        this(new PlainText(author, text), attributes);
    }

    /**
     * Constructor.
     * @param text Text.
     * @param attributes Attributes.
     */
    public TerminalNode(final Text text, final Attributes attributes) {
        this.txt = text;
        this.attrs = attributes;
    }

    @Override
    public Attributes attributes() {
        return this.attrs;
    }

    @Override
    public Node with(final Attributes attributes) {
        return new TerminalNode(
            this.txt,
            this.attrs.add(attributes)
        );
    }

    @Override
    public Text text() {
        return this.txt;
    }

    @Override
    public boolean error() {
        return false;
    }
}
