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
import java.util.List;

/**
 * Rule alternative list.
 * The ruleAltList definition in ANTLR grammar:
 * {@code
 * ruleAltList
 *     : {@link LabeledAlt} (OR {@link LabeledAlt})*
 *     ;
 * }
 * @since 0.1
 */
public final class RuleAltList implements RuleDefinition {

    /**
     * Parent rule.
     */
    private final RuleDefinition parent;

    /**
     * All alternatives of the current node.
     */
    private final List<RuleDefinition> alternatives;

    /**
     * Convergence strategy.
     */
    private final Convergence<RuleDefinition> rand;

    /**
     * Constructor.
     * @param rule Parent rule.
     */
    public RuleAltList(final RuleDefinition rule) {
        this(rule, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param conv Convergence strategy.
     */
    public RuleAltList(final RuleDefinition parent, final Convergence<RuleDefinition> conv) {
        this(parent, new ArrayList<>(0), conv);
    }

    /**
     * Constructor.
     * @param rule Parent rule.
     * @param alts All alternatives of the current node.
     */
    private RuleAltList(final RuleDefinition rule, final List<RuleDefinition> alts) {
        this(rule, alts, new Convergence<>());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param alternatives All alternatives of the current node.
     * @param conv Convergence strategy.
     */
    public RuleAltList(
        final RuleDefinition parent,
        final List<RuleDefinition> alternatives,
        final Convergence<RuleDefinition> conv
    ) {
        this.parent = parent;
        this.alternatives = alternatives;
        this.rand = conv;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (this.alternatives.isEmpty()) {
            throw new IllegalStateException("RuleAltList should have at least one alternative");
        }
        return this.rand.choose(this, this.alternatives).generate();
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.alternatives.add(rule);
    }

    @Override
    public String toString() {
        return String.format(
            "ruleAltList(alternatives=%d, id=%s)",
            this.alternatives.size(),
            System.identityHashCode(this)
        );
    }
}
