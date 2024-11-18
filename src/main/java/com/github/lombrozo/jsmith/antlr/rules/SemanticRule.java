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
package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.semantic.Semantic;
import com.github.lombrozo.jsmith.antlr.semantic.VariableDeclaration;
import com.github.lombrozo.jsmith.antlr.semantic.VariableUsage;
import com.github.lombrozo.jsmith.antlr.semantic.Variables;
import com.github.lombrozo.jsmith.antlr.view.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SemanticRule implements Rule {

    private final Rule origin;
    private final List<String> semantics;
    private final List<Semantic> allowed;

    public SemanticRule(
        final Rule origin,
        final List<String> semantics,
        final Variables variables
    ) {
        this(
            origin,
            new ArrayList<>(semantics),
            Arrays.asList(
                new VariableDeclaration(variables),
                new VariableUsage(variables)
            )
        );
    }

    public SemanticRule(
        final Rule origin,
        final List<String> semantics,
        final List<Semantic> allowed
    ) {
        this.origin = origin;
        this.semantics = semantics;
        this.allowed = allowed;
    }

    @Override
    public Rule parent() {
        return this.origin.parent();
    }

    @Override
    public Text generate(final Context context) {
        final Text original = this.origin.generate(context);
        Text modified = original;
        for (final String semantic : this.semantics) {
            final Semantic found = this.allowed.stream()
                .filter(s -> s.name().equals(semantic))
                .findFirst()
                .orElseThrow(
                    () -> new IllegalStateException(
                        String.format("Semantic '%s' not found", semantic)
                    )
                );
            modified = found.alter(modified);
        }
        return modified;
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
        return new SemanticRule(this.origin.copy(), this.semantics, this.allowed);
    }
}
