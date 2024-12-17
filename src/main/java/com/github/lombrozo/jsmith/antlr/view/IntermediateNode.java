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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Snippet with author.
 * @since 0.1
 */
public final class IntermediateNode implements Node {

    /**
     * Snippets.
     */
    private final List<Node> snippets;

    /**
     * This text labels.
     */
    private final Labels labels;

    /**
     * This snippet attributes.
     */
    private final Attributes attrs;

    /**
     * Constructor.
     * @param author Rule that produces the text.
     * @param snippets Snippets.
     */
    public IntermediateNode(final Rule author, final Node... snippets) {
        this(author, Arrays.asList(snippets));
    }

    /**
     * Constructor.
     * @param author Rule that produces the text.
     * @param snippets Snippets.
     */
    public IntermediateNode(final Rule author, final List<Node> snippets) {
        this(snippets, new Labels(author));
    }

    /**
     * Constructor.
     * @param snippets Snippets.
     * @param labels Labels.
     */
    public IntermediateNode(final List<Node> snippets, final Labels labels) {
        this(
            snippets,
            labels,
            snippets.stream()
                .map(Node::attributes)
                .reduce(new Attributes(), Attributes::add)
        );
    }

    /**
     * Constructor.
     * @param snippets Snippets.
     * @param labels Labels.
     * @param attributes Attributes.
     */
    public IntermediateNode(
        final List<Node> snippets,
        final Labels labels,
        final Attributes attributes
    ) {
        this.snippets = snippets;
        this.labels = labels;
        this.attrs = attributes;
    }

    @Override
    public Attributes attributes() {
        return this.attrs;
    }

    @Override
    public Node with(final Attributes attributes) {
        return new IntermediateNode(
            this.snippets,
            this.labels,
            this.attrs.add(attributes)
        );
    }

    @Override
    public Text text() {
        return new ComposedText(
            this.snippets.stream()
                .map(Node::text)
                .collect(Collectors.toList()),
            this.labels
        );
    }

    @Override
    public boolean isError() {
        return this.snippets.stream().anyMatch(Node::isError);
    }
}
