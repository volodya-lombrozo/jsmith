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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * It is an informative output of the generated text.
 * It is useful for debugging purposes when we need to investigate the output path.
 * @since 0.1
 */
public final class OutputTree {

    /**
     * Original output.
     */
    private final Text original;

    /**
     * Constructor.
     * @param original Original output.
     */
    public OutputTree(final Text original) {
        this.original = original;
    }

    public String output() {
        return this.travers(this.original, 1);
    }

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
                            "\n\t" + Stream.generate(() -> "\t").limit(deep)
                                .collect(Collectors.joining())
                        ))
            );
        }
    }

}
