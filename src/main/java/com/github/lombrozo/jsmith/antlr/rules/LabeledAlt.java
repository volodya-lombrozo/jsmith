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

/**
 * LabeledAlt rule.
 * The ANTLR grammar definition:
 * {@code
 * labeledAlt
 *     : {@link Alternative} (POUND {@link Identifier})?
 *     ;
 * }
 * @since 0.1
 */
public final class LabeledAlt extends Unimplemented {

    /**
     * Production from grammar.
     */
    private final String production;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LabeledAlt(final Rule parent) {
        this(parent, "");
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param production Production from grammar.
     */
    public LabeledAlt(final Rule parent, final String production) {
        super(parent);
        this.production = production;
    }

    @Override
    public String name() {
        return String.format(
            "labeledAlt(id=%d, production='%s')",
            System.identityHashCode(this),
            this.production
        );
    }

    @Override
    public Rule copy() {
        return new LabeledAlt(this.parent());
    }
}

