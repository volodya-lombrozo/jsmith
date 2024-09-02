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

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.random.Convergence;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @since 0.1
 */
public final class Context {
    private final Convergence<Rule> convergence;
    private final List<Rule> chain;

    public Context() {
        this(new Convergence<>());
    }

    public Context(final Convergence<Rule> convergence) {
        this(convergence, new ArrayList<>(0));
    }

    public Context(final Convergence<Rule> convergence, final List<Rule> chain) {
        this.convergence = convergence;
        this.chain = chain;
    }

    public Convergence<Rule> strategy() {
        return this.convergence.copy();
    }

    public Context withConvergence(final Convergence<Rule> convergence) {
        return new Context(convergence, this.chain);
    }

    public Context next(final Rule rule) {
        final List<Rule> chain = new LinkedList<>(this.chain);
        chain.add(rule);
        return new Context(this.convergence, chain);
    }

    public String trace() {
        return this.chain.stream()
            .map(Rule::name)
            .reduce((a, b) -> String.format("%s -> %s", a, b))
            .orElse("");
    }
}