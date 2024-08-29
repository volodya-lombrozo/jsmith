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

import com.github.lombrozo.jsmith.antlr.GenerationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alternative ANTLR rule.
 * The rule definition:
 * {@code
 * alternative
 *     : {@link ElementOptions}? {@link Element}+
 *     |
 *     // explicitly allow empty alts
 *     ;
 * }
 * @since 0.1
 */
public final class Alternative implements RuleDefinition {

    /**
     * Parent rule.
     */
    private final RuleDefinition parent;

    /**
     * Elements of the alternative.
     */
    private final List<RuleDefinition> elements;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public Alternative(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param element Elements of the alternative.
     */
    private Alternative(
        final RuleDefinition parent,
        final List<RuleDefinition> element
    ) {
        this.parent = parent;
        this.elements = element;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate(final GenerationContext context) {
        return this.elements.stream()
            .map(rule -> rule.generate(context))
            .collect(Collectors.joining(" "));
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.elements.add(rule);
    }

    @Override
    public String toString() {
        return "alternative";
    }
}
