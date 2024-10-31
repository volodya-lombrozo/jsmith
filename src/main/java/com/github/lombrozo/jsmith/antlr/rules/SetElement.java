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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SetElement rule.
 * The ANTLR grammar definition:
 * {@code
 * setElement
 *     : TOKEN_REF {@link ElementOptions}?
 *     | STRING_LITERAL {@link ElementOptions}?
 *     | {@link CharacterRange}
 *     | {@link LexerCharSet}
 *     ;
 *  }
 * @since 0.1
 */
public final class SetElement implements Rule, Negatable {

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
    public SetElement(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    private SetElement(final Rule parent, final List<Rule> children) {
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
        return "setElement";
    }

    @Override
    public Text negate(final Context context) {
        if (this.children.isEmpty()) {
            throw new IllegalStateException(
                "SetElement rule is empty, either SetElement or BlockSet should be added before generation"
            );
        }
        if (this.children.stream().anyMatch(rule -> !(rule instanceof Negatable))) {
            throw new IllegalArgumentException(
                String.format(
                    "SetElement rule must contain negatable rules, but found non-negatable rules: [%s]",
                    this.children.stream()
                        .filter(rule -> !(rule instanceof Negatable))
                        .map(Rule::name)
                        .collect(Collectors.joining(", "))
                )
            );
        }
        return new TextNode(
            this,
            this.children.stream()
                .map(Negatable.class::cast)
                .map(negatable -> negatable.negate(context))
                .collect(Collectors.toList())
        );
    }
}
