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
import com.github.lombrozo.jsmith.antlr.view.SignedSnippet;
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import java.util.ArrayList;
import java.util.List;

/**
 * Left to right rule.
 * This rule traverses the rules from left to right and correctly applies the context.
 * @since 0.1
 * @todo #99:90min Implement Hight Level Machine For Traversing Rules.
 *  Implement a high-level machine that will traverse the rules and apply correct context to them.
 *  Currently we are required to add classes like {@link LeftToRight} to traverse the rules.
 *  This is not a good approach and we need to implement a high-level machine that will do this.
 *  This should simplify the code and make it more readable.
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
    public Snippet generate(final Context context) {
        Context current = context;
        final List<Snippet> res = new ArrayList<>(this.all.size());
        for (final Rule rule : this.all) {
            final Snippet snippet = rule.generate(current);
            res.add(snippet);
            current = current.withAttributes(snippet.attributes());
        }
        return new SignedSnippet(this.author, res);
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
