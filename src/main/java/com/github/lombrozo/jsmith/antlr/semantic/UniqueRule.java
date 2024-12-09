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
import com.github.lombrozo.jsmith.antlr.Snippet;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.jcabi.log.Logger;
import java.util.HashSet;
import java.util.Set;

/**
 * Unique Rule.
 * @since 0.1
 */
public final class UniqueRule implements Rule {

    /**
     * Comment to activate this rule.
     */
    public static final String COMMENT = "$jsmith-unique";

    /**
     * Origin rule.
     */
    private final Rule original;

    /**
     * All identifiers.
     */
    private final Set<String> all;

    /**
     * Constructor.
     * @param original Origin rule.
     * @param all All identifiers.
     */
    public UniqueRule(final Rule original, final Set<String> all) {
        this.original = original;
        this.all = all;
    }

    @Override
    public Rule parent() {
        return this.original.parent();
    }

    @Override
    public Snippet generate(final Context context) {
        final Snippet snippet = this.original.generate(context);
        final Snippet result;
        if (this.all.contains(snippet.text().output())) {
            final Snippet reattempt = this.generate(context);
            Logger.info(
                this,
                String.format(
                    "Collision happened: identifier '%s' was already generated, regenerate it to the '%s'.",
                    snippet.text().output(),
                    reattempt.text().output()
                )
            );
            result = reattempt;
        } else {
            this.all.add(snippet.text().output());
            result = snippet;
        }
        return result;
    }

    @Override
    public void append(final Rule rule) {
        this.original.append(rule);
    }

    @Override
    public String name() {
        return String.format("%s(%s)", UniqueRule.COMMENT, this.original.name());
    }

    @Override
    public Rule copy() {
        return new UniqueRule(this.original.copy(), new HashSet<>(0));
    }
}
