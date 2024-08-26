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

/**
 * Atom.
 * The ANTLR grammar definition:
 * {@code
 * atom
 *     : {@link TerminalDef}
 *     | {@link Ruleref}
 *     | notSet
 *     | DOT elementOptions?
 *     ;
 * }
 * @since 0.1
 */
public final class Atom implements RuleDefinition {

    /**
     * Parent rule.
     */
    private final RuleDefinition parent;

    /**
     * Atom inner element.
     */
    private RuleDefinition item;

    /**
     * Constructor.
     * @param rule Parent rule.
     */
    public Atom(final RuleDefinition rule) {
        this.parent = rule;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.item = rule;
    }

    @Override
    public String generate() {
        return this.item.generate();
    }

    @Override
    public String toString() {
        return "atom";
    }
}
