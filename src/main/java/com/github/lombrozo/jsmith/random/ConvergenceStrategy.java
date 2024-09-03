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
package com.github.lombrozo.jsmith.random;

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.List;

/**
 * Convergence strategy.
 * Here is the simplest implementation of the {@link ChoosingStrategy}.
 * It uses the {@link Convergence} to choose the next rule.
 * Pay attention that the single instance of the strategy must be used only for single generation
 * branch.
 * So, we need to copy the strategy for each new generation branch.
 * Otherwise, the convergence state will be shared between the branches
 * which will lead to the infinite recursion.
 * You can read more about this implementation from this interesting
 * <a href="https://eli.thegreenplace.net/2010/01/28/generating-random-sentences-from-a-context-free-grammar/">article</a>.
 * @since 0.1
 */
public final class ConvergenceStrategy implements ChoosingStrategy {

    /**
     * Convergence state.
     */
    private final Convergence<Rule> convergence;

    /**
     * The default constructor.
     */
    public ConvergenceStrategy() {
        this(new Convergence<>());
    }

    /**
     * Constructor.
     * @param convergence The convergence state.
     */
    public ConvergenceStrategy(final Convergence<Rule> convergence) {
        this.convergence = convergence;
    }

    @Override
    public Rule choose(final Rule parent, final List<Rule> children) {
        return this.convergence.choose(parent, children);
    }

    @Override
    public ChoosingStrategy copy() {
        return new ConvergenceStrategy(this.convergence.copy());
    }
}
