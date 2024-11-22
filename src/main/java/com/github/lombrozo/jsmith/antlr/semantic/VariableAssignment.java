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

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.view.Error;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextLeaf;
import com.github.lombrozo.jsmith.antlr.view.TextNode;
import java.util.Collections;
import java.util.Optional;

public final class VariableAssignment implements Rule {

    /**
     * Key for this semantic.
     */
    public static final String KEY = "$jsmith-variable-assignment";

    private final Rule origin;

    private final Variables variables;

    public VariableAssignment(final Rule origin, final Variables variables) {
        this.origin = origin;
        this.variables = variables;
    }

    @Override
    public Rule parent() {
        return this.origin.parent();
    }

    @Override
    public Text generate(final Context context) {
        Text text = this.origin.generate(context);
        if (!text.error()) {
            final Optional<String> declared = this.variables.declared();
            if (declared.isPresent()) {
                text = new TextLeaf(
                    this,
                    declared.get(),
                    Collections.singletonMap("initialized-variable", declared.get())
                );
            } else {
                text = new Error(this, "<variable is not declared yet>");
            }
        }
        return text;
    }

    @Override
    public void append(final Rule rule) {
        this.origin.append(rule);
    }

    @Override
    public String name() {
        return this.origin.name();
    }

    @Override
    public Rule copy() {
        return new VariableAssignment(this.origin.copy(), this.variables);
    }
}
