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
import com.github.lombrozo.jsmith.random.Multiplier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BlockSuffix rule.
 * The ANTLR grammar definition:
 * {@code
 * blockSuffix
 *     : {@link EbnfSuffix}
 *     ;
 * }
 * @since 0.1
 */
public final class BlockSuffix implements Rule, Suffix {

    /**
     * This rule name.
     */
    private static final String NAME = "blockSuffix";

    /**
     * Parent rule.
     */
    private final Rule parent;

    /**
     * Children rules.
     */
    private final List<Rule> children;

    /**
     * Constructor.
     */
    public BlockSuffix() {
        this(new Root());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public BlockSuffix(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    public BlockSuffix(final Rule parent, final List<Rule> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public Text generate(final Context context) {
        return new Text(
            this,
            this.children.stream()
                .map(rule -> rule.generate(context))
                .collect(Text.joining())
        );
    }

    @Override
    public void append(final Rule rule) {
        this.children.add(rule);
    }

    @Override
    public String name() {
        return BlockSuffix.NAME;
    }

    /**
     * Get multiplier.
     * @return Multiplier.
     */
    @Override
    public Multiplier multiplier() {
        if (this.children.isEmpty()) {
            return new Multiplier.One();
        } else {
            final Rule first = this.children.get(0);
            if (first instanceof Suffix) {
                return ((Suffix) first).multiplier();
            } else {
                throw new IllegalStateException(
                    String.format("Unknown block suffix type: %s", first.name())
                );
            }
        }
    }

    /**
     * Check if the rule is BlockSuffix.
     * @param rule Rule.
     * @return True if the rule is BlockSuffix.
     */
    public static boolean is(final Rule rule) {
        return rule.name().equals(BlockSuffix.NAME);
    }
}
