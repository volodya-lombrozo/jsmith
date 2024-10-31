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
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextNode;
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
    private static final String ALIAS = "blockSuffix";

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Children rules.
     */
    private final List<Rule> children;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public BlockSuffix(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     */
    BlockSuffix() {
        this(new Root());
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    private BlockSuffix(final Rule parent, final List<Rule> children) {
        this.top = parent;
        this.children = children;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Text generate(final Context context) {
        return new TextNode(
            this,
            this.children.stream()
                .map(rule -> rule.generate(context))
                .collect(Collectors.toList())
        );
    }

    @Override
    public void append(final Rule rule) {
        this.children.add(rule);
    }

    @Override
    public String name() {
        return BlockSuffix.ALIAS;
    }

    /**
     * Get multiplier.
     * @return Multiplier.
     */
    @Override
    public Multiplier multiplier() {
        final Multiplier result;
        if (this.children.isEmpty()) {
            result = new Multiplier.One();
        } else {
            final Rule first = this.children.get(0);
            if (first instanceof Suffix) {
                result = ((Suffix) first).multiplier();
            } else {
                throw new IllegalStateException(
                    String.format("Unknown block suffix type: %s", first.name())
                );
            }
        }
        return result;
    }

    /**
     * Check if the rule is BlockSuffix.
     * @param rule Rule.
     * @return True if the rule is BlockSuffix.
     */
    static boolean isBlockSuffix(final Rule rule) {
        return BlockSuffix.ALIAS.equals(rule.name());
    }
}
