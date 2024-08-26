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

import com.github.lombrozo.jsmith.antlr.Unlexer;
import com.github.lombrozo.jsmith.antlr.Unparser;

/**
 * Terminal definition.
 * The ANTLR grammar definition:
 * {@code
 * terminalDef
 *     : TOKEN_REF elementOptions?
 *     | STRING_LITERAL elementOptions?
 *     ;
 * }
 * @since 0.1
 */
public final class TerminalDef implements RuleDefinition {

    /**
     * Parent rule.
     */
    private final RuleDefinition parent;

    /**
     * Unlexer.
     */
    private final Unlexer unlexer;

    /**
     * Text.
     */
    private final String text;

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param unlexer Unparser.
     * @param text Text.
     */
    public TerminalDef(final RuleDefinition parent, final Unlexer unlexer, final String text) {
        this.parent = parent;
        this.unlexer = unlexer;
        this.text = text;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        final LexerRuleSpec rule = this.unlexer.find(this.text);
        //todo: fix null check - remove it!
        if (rule == null) {
            return this.text.replace("'", "");
        }
        return rule.generate();
    }

    @Override
    public void append(final RuleDefinition rule) {
        throw new UnsupportedOperationException("Terminal cannot have children yet");
    }

    @Override
    public String toString() {
        return String.format("terminalDef(%s)", this.text);
    }
}
