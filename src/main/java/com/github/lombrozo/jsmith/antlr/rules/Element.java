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
import com.github.lombrozo.jsmith.random.Multiplier;
import java.util.ArrayList;
import java.util.List;

/**
 * Alternative elements.
 * The ANTLR grammar definition:
 * {@code
 * element
 *     : {@link LabeledElement} ({@link EbnfSuffix} |)
 *     | {@link Atom} ({@link EbnfSuffix} |)
 *     | {@link Ebnf}
 *     | {@link ActionBlock} (QUESTION {@link PredicateOptions}?)?
 *     ;
 * }
 * @since 0.1
 */
public final class Element implements Rule {

    /**
     * Parent rule.
     */
    private final Rule parent;

    /**
     * Children rules.
     */
    private List<Rule> children;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public Element(final Rule parent) {
        this.parent = parent;
        this.children = new ArrayList<>(1);
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public String generate(final Context context) {
        if (this.children.isEmpty()) {
            throw new IllegalStateException("Element should have at least one child");
        }
        final String result;
        final Rule first = this.children.get(0);
        if (first instanceof Atom) {
            result = this.multiplier().repeat(first).generate(context);
        } else if (first instanceof LabeledElement) {
            result = this.multiplier().repeat(first).generate(context);
        } else if (first instanceof Ebnf) {
            result = first.generate(context);
        } else {
            throw new IllegalStateException(
                String.format("Unrecognized element type '%s' for '%s' element", first, this)
            );
        }
        return result;
    }

    @Override
    public void append(final Rule rule) {
        this.children.add(rule);
    }

    @Override
    public String name() {
        return "element";
    }

    /**
     * Returns the multiplier for the element.
     * This multiplier might be defined for {@link Atom} and {@link LabeledElement} child elements.
     * @return The multiplier for the {@link Atom} and {@link LabeledElement}.
     */
    private Multiplier multiplier() {
        final Multiplier result;
        if (this.children.size() == 1) {
            result = new Multiplier.One();
        } else {
            result = ((EbnfSuffix) this.children.get(1)).multiplier();
        }
        return result;
    }
}
