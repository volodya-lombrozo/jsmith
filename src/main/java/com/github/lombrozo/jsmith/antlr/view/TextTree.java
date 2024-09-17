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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * It is an informative output of the generated text.
 * It is useful for debugging purposes when we need to investigate the output path.
 * @since 0.1
 */
public final class TextTree implements Text {

    /**
     * Original output.
     */
    private final Text original;

    /**
     * Constructor.
     * @param original Original output.
     */
    public TextTree(final Text original) {
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
        return this.travers(this.original, 1);
    }

    /**
     * Recursive traversing of the tree.
     * @param current Current node.
     * @param deep Deep of the current node.
     * @return String representation of the tree.
     */
    private String travers(final Text current, final int deep) {
        if (current.children().isEmpty()) {
            return String.format("%s: '%s'", current.writer().name(), current.output());
        } else {
            return String.format(
                "%s\t%s",
                current.writer().name(),
                current.children().stream()
                    .map(child -> this.travers(child, deep + 1))
                    .collect(
                        Collectors.joining(
                            String.format(
                                "\n\t%s",
                                Stream.generate(() -> "\t")
                                    .limit(deep)
                                    .collect(Collectors.joining())
                            )
                        )
                    )
            );
        }
    }

}
