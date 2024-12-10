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

import com.github.lombrozo.jsmith.antlr.Attributes;
import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.ErrorSnippet;
import com.github.lombrozo.jsmith.antlr.LeafSnippet;
import com.github.lombrozo.jsmith.antlr.Snippet;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.jcabi.log.Logger;
import java.util.Optional;

/**
 * Variable Usage Semantic.
 * Adds variable usage to the context.
 * @since 0.1
 */
public final class VariableUsage implements Rule {

    /**
     * Comment to activate this rule.
     */
    public static final String COMMENT = "$jsmith-var-use";

    /**
     * Origin rule.
     */
    private final Rule origin;

    /**
     * Constructor.
     * @param origin Origin rule.
     */
    public VariableUsage(final Rule origin) {
        this.origin = origin;
    }

    @Override
    public Rule parent() {
        return this.origin.parent();
    }

    @Override
    public Snippet generate(final Context context) {
        final Snippet snippet = this.origin.generate(context);
        final Optional<String> initialized;
        final Attributes attributes = context.attributes();
        if (attributes.contains(TypeRule.COMMENT)) {
            initialized = context.current().initialized(attributes.get(TypeRule.COMMENT));
        } else {
            initialized = context.current().initialized();
        }
        return initialized
            .map(output -> (Snippet) new LeafSnippet(snippet.text().writer(), output))
            .orElseGet(
                () -> {
                    Logger.warn(
                        this,
                        String.format(
                            "We cannot find any initialized variable in the scope '%s'",
                            context.current()
                        )
                    );
                    return new ErrorSnippet(snippet.text().writer(), "<variable not found>");
                }
            );
    }

    @Override
    public void append(final Rule rule) {
        this.origin.append(rule);
    }

    @Override
    public String name() {
        return VariableUsage.COMMENT;
    }

    @Override
    public Rule copy() {
        return new VariableUsage(this.origin.copy());
    }
}
