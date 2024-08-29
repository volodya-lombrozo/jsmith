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

import com.github.lombrozo.jsmith.Convergence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Alternative ANTLR rule.
 * The rule definition:
 * {@code
 * altList
 *     : {@link Alternative} (OR {@link Alternative})*
 *     ;
 * }
 * @since 0.1
 */
public final class AltList implements RuleDefinition {

    /**
     * Parent rule.
     */
    private final RuleDefinition parent;

    /**
     * Alternatives.
     */
    private final List<RuleDefinition> alternatives;

    /**
     * Convergence strategy.
     * Allows choosing a random alternative based on the history of the previous choices.
     */
    private final Convergence<RuleDefinition> rand;

    /**
     * Default constructor.
     */
    public AltList() {
        this(new Root());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public AltList(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param rand Convergence strategy.
     */
    public AltList(final RuleDefinition parent, final Convergence rand) {
        this(parent, new ArrayList<>(0), rand);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param alternatives Alternatives.
     */
    AltList(final RuleDefinition parent, RuleDefinition... alternatives) {
        this(parent, Arrays.asList(alternatives));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param alternatives Alternatives.
     */
    private AltList(final RuleDefinition parent, final List<RuleDefinition> alternatives) {
        this(parent, alternatives, new Convergence<>());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param alternatives Alternatives.
     * @param rand Convergence strategy.
     */
    private AltList(
        final RuleDefinition parent,
        final List<RuleDefinition> alternatives,
        final Convergence<RuleDefinition> rand
    ) {
        this.parent = parent;
        this.alternatives = alternatives;
        this.rand = rand;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        final String result;
        if (this.alternatives.isEmpty()) {
            result = "";
        } else {
            result = this.rand.choose(this, this.alternatives).generate();
        }
        return result;
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.alternatives.add(rule);
    }

    @Override
    public String toString() {
        return String.format("altList(size=%d)", this.alternatives.size());
    }
}
