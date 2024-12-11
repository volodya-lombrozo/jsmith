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

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class represents a node of the generated text tree.
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
public final class TextSequence implements Text {

    /**
     * Default delimiter.
     */
    private static final String DELIMITER = "";

    /**
     * Delimiter between children.
     */
    private final String delimiter;

    /**
     * Children of the node.
     */
    private final List<Text> children;

    /**
     * Labels of the node.
     */
    private final Labels labels;

    /**
     * Constructor.
     * @param writer Rule that writes the text.
     * @param children Children of the node.
     */
    public TextSequence(final Rule writer, final List<Text> children) {
        this(writer, children, TextSequence.DELIMITER);
    }

    /**
     * Constructor.
     * @param childs Children of the node.
     * @param labels Labels of the node.
     */
    public TextSequence(final List<Text> childs, final Labels labels) {
        this(childs, TextSequence.DELIMITER, labels);
    }

    /**
     * Constructor.
     * @param writer Rule that writes the text.
     * @param children Children of the node.
     * @param delimiter Delimiter between children.
     */
    private TextSequence(final Rule writer, final List<Text> children, final String delimiter) {
        this(children, delimiter, new Labels(writer));
    }

    /**
     * Constructor.
     * @param childs Children of the node.
     * @param delimiter Delimiter between children.
     * @param labels Labels of the node.
     */
    private TextSequence(
        final List<Text> childs,
        final String delimiter,
        final Labels labels
    ) {
        this.children = childs;
        this.delimiter = delimiter;
        this.labels = labels;
    }

    @Override
    public List<Text> children() {
        return Collections.unmodifiableList(this.children);
    }

    @Override
    public String output() {
        return this.children.stream()
            .map(Text::output)
            .collect(Collectors.joining(this.delimiter));
    }

    @Override
    public Labels labels() {
        return this.labels;
    }
}
