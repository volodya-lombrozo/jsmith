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
package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.random.Convergence;
import com.github.lombrozo.jsmith.random.Rand;
import com.github.lombrozo.jsmith.antlr.rules.Rule;

/**
 *
 * @since 0.1
 */
public final class GenerationContext {
    private final Rand rand;
    private final Convergence<Rule> convergence;

    public GenerationContext() {
        this(new Rand(), new Convergence<>());
    }

    public GenerationContext(
        final Rand rand,
        final Convergence<Rule> convergence
    ) {
        this.rand = rand;
        this.convergence = convergence;
    }

    public Rand rand() {
        return this.rand;
    }

    public Convergence<Rule> convergence() {
        return this.convergence.copy();
    }

    public GenerationContext withConvergence(final Convergence<Rule> convergence) {
        return new GenerationContext(this.rand, convergence);
    }

}
