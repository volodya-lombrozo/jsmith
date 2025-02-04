/*
 * MIT License
 *
 * Copyright (c) 2023-2025 Volodya Lombrozo
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
import com.github.lombrozo.jsmith.antlr.view.IntermediateNode;
import com.github.lombrozo.jsmith.antlr.view.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Left to right rule.
 * This rule traverses the rules from left to right and correctly applies the context.
 * I've tried to rewrite this class to some sort of machine that would traverse the rules and
 * apply the context to them, but failed.
 * The solution with machine looks pretty overcomplicated, at least with the current design.
 * You can check it here: https://github.com/volodya-lombrozo/jsmith/tree/106_machine
 * So, we need to keep this class to correctly transfer context from one rule to another, from
 * left to right.
 * @since 0.1
 */
public final class LeftToRight implements Rule {

    /**
     * Original rule.
     */
    private final Rule author;

    /**
     * Child rules.
     */
    private final List<Rule> all;

    /**
     * Constructor.
     * @param author Original rule.
     * @param all Child rules.¬
     */
    public LeftToRight(final Rule author, final List<Rule> all) {
        this.author = author;
        this.all = all;
    }

    @Override
    public Rule parent() {
        throw new UnsupportedOperationException(
            "Operation 'parent' is not supported in LeftToRight"
        );
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        Context current = context;
        final List<Node> res = new ArrayList<>(this.all.size());
        for (final Rule rule : this.all) {
            final Node snippet = rule.generate(current);
            res.add(snippet);
            current = current.withAttributes(snippet.attributes());
        }
        return new IntermediateNode(this.author, res);
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException(
            "Operation 'append' is not supported in LeftToRight"
        );
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException(
            "Operation 'name' is not supported in LeftToRight"
        );
    }

    @Override
    public Rule copy() {
        throw new UnsupportedOperationException(
            "Operation 'copy' is not supported in LeftToRight"
        );
    }
}
