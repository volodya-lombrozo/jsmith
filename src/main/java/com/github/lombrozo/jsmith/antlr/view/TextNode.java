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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public final class TextNode implements Text {

    /**
     * Default delimiter.
     */
    private static final String DELIMITER = "";
    private final Rule writer;
    private final List<Text> children;
    private final String delimiter;

    public TextNode(final Rule writer, final Text... children) {
        this(writer, Arrays.asList(children));
    }

    public TextNode(final Rule writer, final List<Text> children) {
        this(writer, children, TextNode.DELIMITER);
    }

    public TextNode(final Rule writer, final List<Text> children, final String delimiter) {
        this.writer = writer;
        this.children = children;
        this.delimiter = delimiter;
    }

    @Override
    public Rule writer() {
        return this.writer;
    }

    @Override
    public List<Text> children() {
        return Collections.unmodifiableList(this.children);
    }

    @Override
    public String output() {
        return
            this.children.stream()
                .map(Text::output)
                .collect(Collectors.joining(this.delimiter));
    }
}
