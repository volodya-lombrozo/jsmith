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
package com.github.lombrozo.jsmith.antlr.semantic;

import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextLeaf;

/**
 * Variable Usage Semantic.
 * Adds variable usage to the context.
 * @since 0.1
 */
public final class VariableUsage implements Semantic {

    /**
     * All declared variables.
     */
    private final Variables variables;

    /**
     * Constructor.
     * @param variables All declared variables.
     */
    public VariableUsage(final Variables variables) {
        this.variables = variables;
    }

    @Override
    public Text alter(final Text text) {
        //TODO: WRONG IMPLEMNTATION
        return this.variables.retrieve()
            .map(var -> new TextLeaf(text.writer(), var))
            .orElse(new TextLeaf(text.writer(), text.output()));
//        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String name() {
        return "$jsmith-variable-usage";
    }
}
