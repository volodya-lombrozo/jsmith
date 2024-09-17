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
import java.util.List;

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
        return String.format("%s -----> %s", path(this.original.writer()), this.original.output());
    }

    List<Rule> path(final Rule rule) {
        List<Rule> res = new ArrayList<>();
        res.add(rule);
        if (rule.parent() == rule) {
            return res;
        } else {
            res.addAll(path(rule.parent()));
        }
        return res;
    }
}
