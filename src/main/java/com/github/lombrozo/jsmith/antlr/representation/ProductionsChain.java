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
package com.github.lombrozo.jsmith.antlr.representation;

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Chain of {@link Rule} objects.
 * Used for logging of the current chain of {@link Rule} objects.
 * @since 0.1
 */
public final class ProductionsChain {

    /**
     * The lowest child.
     */
    private final Rule child;

    /**
     * Constructor.
     * @param start The lowest child.
     */
    public ProductionsChain(final Rule start) {
        this.child = start;
    }

    /**
     * Tree representation of a production chain.
     * Looks like:
     * abcd
     *   └──efgh
     *        └──lowest
     * @return Tree-like string representation of a chain.
     */
    public String tree() {
        return this.tree(this.child);
    }

    /**
     * Prints the tree of the {@link Rule} objects.
     * @param node The current node.
     * @return The tree of the {@link Rule} objects.
     */
    private String tree(final Rule node) {
        final String result;
        if (node == node.parent()) {
            result = String.format("%s\n", node);
        } else {
            final String prev = this.tree(node.parent());
            final int length = prev.split("\n").length;
            result = String.format(
                "%s%s└──%s%n",
                prev,
                Stream.generate(() -> " ")
                    .limit(2 + ((length - 1) * 5L))
                    .collect(Collectors.joining()),
                node
            );
        }
        return result;
    }
}
