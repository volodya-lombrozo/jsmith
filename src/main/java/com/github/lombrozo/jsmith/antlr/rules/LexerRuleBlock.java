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
import com.github.lombrozo.jsmith.guard.Allowed;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lexer rule block.
 * The ANTLR rule definition:
 * {@code
 * lexerRuleBlock
 *     : {@link LexerAltList}
 *     ;
 * }
 * @since 0.1
 */
public final class LexerRuleBlock implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Child rule (LexerAltList).
     */
    private final AtomicReference<Rule> child;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LexerRuleBlock(final Rule parent) {
        this(parent, new AtomicReference<Rule>());
    }

    /**
     * Constructor.
     * @param parent Parent rule
     * @param child Child rule
     */
    public LexerRuleBlock(final Rule parent, final AtomicReference<Rule> child) {
        this.top = parent;
        this.child = child;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        return new IntermediateNode(this, this.child.get().generate(context));
    }

    @Override
    public void append(final Rule rule) {
        new Allowed("lexerAltList").check(rule);
        this.child.set(rule);
    }

    @Override
    public String name() {
        return "lexerRuleBlock";
    }

    @Override
    public Rule copy() {
        return new LexerRuleBlock(this.parent(), new AtomicReference<>(this.child.get().copy()));
    }
}
