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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LexerCommands rule.
 * The ANTLR grammar definition:
 * {@code
 * lexerCommands
 *     : RARROW {@link LexerCommand} (COMMA {@link LexerCommand})*
 *     ;
 * }
 *
 * @since 0.1
 *   @todo #150:90min We have a lot of repeated checks in append methods.
 *   It's better to create class for this type of checks.
 *   Something like {@code new Allowed("lexerCommand", "RARROW", "COMMA").check(this);}
 */
public final class LexerCommands implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * List of lexer commands.
     */
    private final List<Rule> elements;

    /**
     * Constructor.
     *
     * @param parent Parent rule.
     */
    public LexerCommands(final Rule parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     *
     * @param parent   Parent rule
     * @param elements List of elements
     */
    public LexerCommands(final Rule parent, final List<Rule> elements) {
        this.top = parent;
        this.elements = elements;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        if (this.elements.isEmpty()) {
            throw new IllegalStateException("LexerCommands can't be empty");
        }
        return new LeftToRight(this, this.elements).generate(context);
    }

    @Override
    public void append(final Rule rule) {
        if (
            !"lexerCommand".equals(rule.name())
                && !rule.name().contains("RARROW")
                && !rule.name().contains("COMMA")
        ) {
            throw new IllegalArgumentException(
                String.format("Rule %s can't be appended to LexerCommands", rule.name())
            );
        }
        this.elements.add(rule);
    }

    @Override
    public String name() {
        return "lexerCommands";
    }

    @Override
    public Rule copy() {
        return new LexerCommands(
            this.parent(),
            this.elements.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }
}
