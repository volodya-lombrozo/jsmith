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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Rule block.
 * The ruleBlock definition in ANTLR grammar:
 * {@code
 * ruleBlock
 *     : {@link RuleAltList}
 *     ;
 * }
 * @since 0.1
 */
@SuppressWarnings("PMD.OnlyOneConstructorShouldDoInitialization")
public final class RuleBlock implements Rule {
    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * RuleAltList.
     */
    private final AtomicReference<Rule> list;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public RuleBlock(final Rule parent) {
        this.top = parent;
        this.list = new AtomicReference<>();
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        return new IntermediateNode(this, this.list.get().generate(context));
    }

    @Override
    public void append(final Rule rule) {
        if (!rule.name().contains("ruleAltList")) {
            throw new IllegalArgumentException("Child of rule block must be RuleAltList");
        }
        this.list.set(rule);
    }

    @Override
    public String name() {
        return "ruleBlock";
    }

    @Override
    public Rule copy() {
        final RuleBlock cpy = new RuleBlock(this.top);
        cpy.append(this.list.get().copy());
        return cpy;
    }
}
