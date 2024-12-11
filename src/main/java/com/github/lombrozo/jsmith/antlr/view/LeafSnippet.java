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
package com.github.lombrozo.jsmith.antlr.view;

import com.github.lombrozo.jsmith.antlr.Attributes;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.concurrent.atomic.AtomicReference;

public final class LeafSnippet implements Snippet {

    private final Text text;

    private final Attributes attributes;

    public LeafSnippet(final Rule author, final String text) {
        this(new TextLeaf(author.name(), text));
    }

    public LeafSnippet(final String author, final String text) {
        this(new TextLeaf(author, text));
    }

    public LeafSnippet(final Text text) {
        this(text, new Attributes());
    }

    public LeafSnippet(final String author, final String text, final Attributes attributes) {
        this(new TextLeaf(author, text), attributes);
    }

    public LeafSnippet(final Text text, final Attributes attributes) {
        this.text = text;
        this.attributes = attributes;
    }

    @Override
    public Attributes attributes() {
        return this.attributes;
    }

    @Override
    public Text text() {
        return this.text;
    }

    @Override
    public boolean isError() {
        return false;
    }
}
