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
import com.github.lombrozo.jsmith.antlr.Text;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Cage rule.
 * This rule is used to catch the context.
 * Used in tests.
 * @since 0.1
 */
final class Cage implements Rule {

    /**
     * Trapped context.
     */
    private final AtomicReference<Context> trap;

    /**
     * Default constructor.
     */
    Cage() {
        this(new AtomicReference<>());
    }

    /**
     * Constructor.
     * @param trap Trapped context.
     */
    private Cage(final AtomicReference<Context> trap) {
        this.trap = trap;
    }

    @Override
    public Rule parent() {
        throw new UnsupportedOperationException("Cage node doesn't have a parent node");
    }

    @Override
    public Text generate(final Context context) {
        this.trap.set(context);
        return new Text(this, "");
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("Cage node cannot add children");
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException("Cage node doesn't have a name");
    }

    /**
     * Get trapped context.
     * @return The context.
     */
    Context trapped() {
        if (Objects.isNull(this.trap.get())) {
            throw new IllegalStateException("Cage didn't catch anything");
        }
        return this.trap.get();
    }
}
