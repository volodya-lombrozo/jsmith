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
import com.github.lombrozo.jsmith.antlr.semantic.Scope;
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
    private final ChoosingStrategy strat;

    /**
     * Path of the rules that were visited during the generation.
     */
    private final List<Rule> visited;

    /**
     * Current scope.
     */
    private final Scope scope;

    /**
     * Constructor.
     * Uses the default {@link ConvergenceStrategy}.
     */
    public Context() {
        this(new ConvergenceStrategy());
    }

    /**
     * Constructor.
     * @param scope The scope.
     * @param convergence The convergence strategy.
     */
    public Context(final Scope scope, final ConvergenceStrategy convergence) {
        this(convergence, new ArrayList<>(0), scope);
    }

    /**
     * Constructor.
     * @param chain The path of the rules that were visited during the generation.
     */
    public Context(final List<Rule> chain) {
        this(new ConvergenceStrategy(), chain);
    }

    /**
     * Constructor.
     * @param strat The strategy used in the generation.
     * @param visited The path of the rules that were visited during the generation.
     * @param scope The scope.
     */
    public Context(
        final ChoosingStrategy strat,
        final List<Rule> visited,
        final Scope scope
    ) {
        this.strat = strat;
        this.visited = visited;
        this.scope = scope;
    }

    /**
     * Constructor.
     * @param strat The strategy used in the generation.
     * @param chain The path of the rules that were visited during the generation.
     */
    private Context(final ChoosingStrategy strat, final List<Rule> chain) {
        this(strat, chain, null);
    }

    /**
     * Constructor.
     * @param strategy The strategy used in the generation.
     */
    private Context(final ChoosingStrategy strategy) {
        this(strategy, new ArrayList<>(0));
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
            this.strat.copy(),
            Stream.concat(
                this.visited.stream(),
                Stream.of(rule)
            ).collect(Collectors.toList()),
            this.scope
        );
    }

    /**
     * Returns the next context with another scope.
     * @param another The scope.
     * @return The next context with the scope.
     */
    public Context withScope(final Scope another) {
        return new Context(this.strat, this.visited, another);
    }

    /**
     * Returns the current scope.
     * @return The scope.
     */
    public Scope current() {
        return this.scope;
    }

    /**
     * Returns the strategy used in the generation.
     * @return The strategy used in the generation.
     */
    public ChoosingStrategy strategy() {
        return this.strat;
    }

    /**
     * Returns the path of the rules that were visited during the generation.
     * @return The path of the rules that were visited during the generation.
     */
    public List<Rule> path() {
        return Collections.unmodifiableList(this.visited);
    }
}
