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
import com.github.lombrozo.jsmith.antlr.view.TerminalNode;

/**
 * LexerCommandName rule.
 * The ANTLR grammar definition:
 * {@code
 * lexerCommandName
 *     : {@link Identifier}
 *     | MODE
 *     ;
 * }
 * @since 0.1
 */
public final class LexerCommandName implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Lexer command name. It should be identifier or MODE.
     */
    private final String ref;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LexerCommandName(final Rule parent) {
        this(parent, "");
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param name Name of a lexer command.
     */
    public LexerCommandName(final Rule parent, final String name) {
        this.top = parent;
        this.ref = name;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) throws WrongPathException {
        return new TerminalNode(this, this.ref);
    }

    @Override
    public void append(final Rule rule) {
        // Should not do anything.
    }

    @Override
    public String name() {
        return "lexerCommandName";
    }

    @Override
    public Rule copy() {
        return new LexerCommandName(this.parent(), this.ref);
    }
}
