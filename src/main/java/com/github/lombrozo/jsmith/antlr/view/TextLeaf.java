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

import com.github.lombrozo.jsmith.antlr.rules.Empty;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class represents a leaf of the generated text tree.
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
public final class TextLeaf implements Text {
    /**
     * Who writes the text.
     */
    private final Rule author;

    /**
     * Text output produced by {@link #author}.
     */
    private final String text;

    /**
     * Additional attributes of the text.
     */
    private final Map<String, String> additional;

    /**
     * Default constructor.
     * @param output Text output.
     */
    public TextLeaf(final String output) {
        this(new Empty(), output);
    }

    /**
     * Constructor.
     * @param writer Author of the text.
     * @param output Text output.
     */
    public TextLeaf(final Rule writer, final String output) {
        this(writer, output, new HashMap<>(0));
    }

    public TextLeaf(
        final Rule author,
        final String text,
        final Map<String, String> additional
    ) {
        this.author = author;
        this.text = text;
        this.additional = additional;
    }

    @Override
    public Rule writer() {
        return this.author;
    }

    @Override
    public List<Text> children() {
        return Collections.emptyList();
    }

    @Override
    public String output() {
        return this.text;
    }

    @Override
    public Attributes attributes() {
        return new Attributes(true, this.additional);
    }

    @Override
    public boolean error() {
        return false;
    }
}
