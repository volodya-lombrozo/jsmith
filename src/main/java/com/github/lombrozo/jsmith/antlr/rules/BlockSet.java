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
import com.github.lombrozo.jsmith.antlr.view.Node;
import com.github.lombrozo.jsmith.guard.Allowed;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BlockSet rule.
 * The ANTLR grammar definition:
 * {@code
 * blockSet
 *     : LPAREN {@link SetElement} (OR {@link SetElement})* RPAREN
 *     ;
 * }
 * @since 0.1
 */
public final class BlockSet implements Rule {
    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * List of elements.
     */
    private final List<Rule> elems;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public BlockSet(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule
     * @param elems List of elements
     */
    public BlockSet(final Rule parent, final List<Rule> elems) {
        this.top = parent;
        this.elems = elems;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        if (this.elems.size() < 2) {
            throw new IllegalStateException("BlockSet should have at least three elements");
        }
        return new LeftToRight(this, this.elems).generate(context);
    }

    @Override
    public void append(final Rule rule) {
        new Allowed("LPAREN", "setElement", "RPAREN").check(rule);
        this.elems.add(rule);
    }

    @Override
    public String name() {
        return "blockSet";
    }

    @Override
    public Rule copy() {
        return new BlockSet(
            this.parent(),
            this.elems.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }
}
