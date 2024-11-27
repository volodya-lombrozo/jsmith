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
     * Name for this rule.
     */
    public static final String KEY = "$jsmith-unique";

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
     */
    public UniqueRule(final Rule original) {
        this(original, new HashSet<>(0));
    }

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
    public Text generate(final Context context) {
        final Text text = this.original.generate(context);
        if (this.all.contains(text.output())) {
            final Text second = this.generate(context);
            Logger.info(
                this,
                String.format(
                    "Collision happened: identifier '%s' was already generated, regenerate it to the '%s'.",
                    text.output(),
                    second.output()
                )
            );
            return second;
        } else {
            this.all.add(text.output());
        }
        return text;
    }

    @Override
    public void append(final Rule rule) {
        this.original.append(rule);
    }

    @Override
    public String name() {
        return String.format("%s(%s)", UniqueRule.KEY, this.original.name());
    }

    @Override
    public Rule copy() {
        return new UniqueRule(this.original.copy());
    }
}
