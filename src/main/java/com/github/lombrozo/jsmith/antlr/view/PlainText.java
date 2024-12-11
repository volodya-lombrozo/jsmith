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
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class represents a leaf of the generated text tree.
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
public final class PlainText implements Text {

    /**
     * Text output produced by some rule.
     */
    private final String text;

    /**
     * Additional attributes of the text.
     */
    private final Labels labels;

    /**
     * Default constructor.
     * @param output Text output.
     */
    public PlainText(final String output) {
        this(output, new Labels(new Empty()));
    }

    /**
     * Constructor.
     * @param writer Author of the text.
     * @param output Text output.
     */
    PlainText(final Rule writer, final String output) {
        this(output, new Labels(writer));
    }

    /**
     * Constructor.
     * @param writer Author of the text.
     * @param output Text output.
     */
    PlainText(final String writer, final String output) {
        this(output, new Labels(writer));
    }

    /**
     * Constructor.
     * @param text Text output.
     * @param labels Text labels.
     */
    public PlainText(
        final String text,
        final Labels labels
    ) {
        this.text = text;
        this.labels = labels;
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
    public Labels labels() {
        return this.labels;
    }
}
