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
import java.util.List;

/**
 * Text written by a rule.
 * The same as {@link TextNode}, but with a different attributes.
 * @since 0.1
 */
public final class RuleText implements Text {

    /**
     * Original text node.
     */
    private final TextNode original;

    /**
     * Constructor.
     * @param writer The author of the text.
     * @param children Children text nodes.
     */
    public RuleText(final Rule writer, final List<Text> children) {
        this(new TextNode(writer, children));
    }

    /**
     * Constructor.
     * @param original Original text node.
     */
    private RuleText(final TextNode original) {
        this.original = original;
    }

    @Override
    public Rule writer() {
        return this.original.writer();
    }

    @Override
    public List<Text> children() {
        return this.original.children();
    }

    @Override
    public String output() {
        return this.original.output();
    }

    @Override
    public Attributes attributes() {
        return new Attributes(true);
    }
}
