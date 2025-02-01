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
package com.github.lombrozo.jsmith.guard;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

/**
 * ANTLR syntax error listener.
 * This listener is used in {@link SyntaxGuard}.
 * It collects all syntax errors and throws {@link InvalidSyntax} exception if any errors are found.
 * @since 0.1
 */
public final class SyntaxErrorListener implements ANTLRErrorListener {

    /**
     * All syntax errors.
     */
    private final List<String> errors;

    /**
     * Constructor.
     */
    SyntaxErrorListener() {
        this(new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param errors All syntax errors.
     */
    private SyntaxErrorListener(final List<String> errors) {
        this.errors = errors;
    }

    @Override
    public void syntaxError(
        final Recognizer<?, ?> recognizer,
        final Object symbol,
        final int line,
        final int position,
        final String msg,
        final RecognitionException exception
    ) {
        this.errors.add(msg);
    }

    @Override
    public void reportAmbiguity(
        final Parser recognizer,
        final DFA dfa,
        final int start,
        final int stop,
        final boolean exact,
        final BitSet alternatives,
        final ATNConfigSet configs
    ) {
        // We ignore Ambiguity issues
    }

    @Override
    public void reportAttemptingFullContext(
        final Parser recognizer,
        final DFA dfa,
        final int start,
        final int stop,
        final BitSet alternatives,
        final ATNConfigSet configs
    ) {
        // We ignore Full Context issues
    }

    @Override
    public void reportContextSensitivity(
        final Parser recognizer,
        final DFA dfa,
        final int start,
        final int stop,
        final int prediction,
        final ATNConfigSet configs
    ) {
        // We ignore Context Sensitivity issues
    }

    /**
     * Report all syntax errors.
     * @throws InvalidSyntax If any syntax errors are found.
     */
    void report() throws InvalidSyntax {
        if (!this.errors.isEmpty()) {
            throw new InvalidSyntax(String.join("\n", this.errors));
        }
    }
}
