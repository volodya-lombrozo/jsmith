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

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.Arrays;
import java.util.List;

/**
 * Simplest representation of the generation trace.
 * Example of the trace:
 * {@code
 * literal(1) -> literal(2) -> literal(3)
 * }
 * @since 0.1
 */
public final class Trace {

    /**
     * Nodes that were visited during the generation.
     */
    private final List<Rule> visited;

    /**
     * Constructor.
     * @param visited Nodes that were visited during the generation.
     */
    public Trace(final List<Rule> visited) {
        this.visited = visited;
    }

    /**
     * Constructor.
     * @param visited Nodes that were visited during the generation.
     */
    Trace(final Rule... visited) {
        this(Arrays.asList(visited));
    }

    /**
     * Line representation of the trace.
     * @return Line representation of the trace.
     */
    public String line() {
        return this.visited.stream()
            .map(Rule::name)
            .reduce((a, b) -> String.format("%s -> %s", a, b))
            .orElse("");
    }
}
