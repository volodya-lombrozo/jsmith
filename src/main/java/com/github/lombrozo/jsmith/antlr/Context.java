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
import com.github.lombrozo.jsmith.random.ChoosingStrategy;
import com.github.lombrozo.jsmith.random.ConvergenceStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Context of the generation.
 * @since 0.1
 */
public final class Context {

    /**
     * Strategy used in the generation.
     */
    private final ChoosingStrategy strategy;

    /**
     * Path of the rules that were visited during the generation.
     */
    private final List<Rule> visited;

    /**
     * Constructor.
     * Uses the default {@link ConvergenceStrategy}.
     */
    public Context() {
        this(new ConvergenceStrategy());
    }

    /**
     * Constructor.
     * @param strategy The strategy used in the generation.
     */
    public Context(final ChoosingStrategy strategy) {
        this(strategy, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param strat The strategy used in the generation.
     * @param chain The path of the rules that were visited during the generation.
     */
    public Context(final ChoosingStrategy strat, final List<Rule> chain) {
        this.strategy = strat;
        this.visited = chain;
    }

    /**
     * Returns the next context with the rule added to the path.
     * Pay attention that each time when we proceed to the next rule, we create a new context.
     * The new context contains the copy of the {@link ChoosingStrategy} and the new path.
     * @param rule The rule to add to the path.
     * @return The next context with the rule added to the path.
     */
    public Context next(final Rule rule) {
        return new Context(
            this.strategy.copy(),
            Stream.concat(
                this.visited.stream(),
                Stream.of(rule)
            ).collect(Collectors.toList())
        );
    }

    /**
     * Returns the strategy used in the generation.
     * @return The strategy used in the generation.
     */
    public ChoosingStrategy strategy() {
        return this.strategy;
    }

    /**
     * Returns the path of the rules that were visited during the generation.
     * @return The path of the rules that were visited during the generation.
     */
    public List<Rule> path() {
        return Collections.unmodifiableList(this.visited);
    }
}
