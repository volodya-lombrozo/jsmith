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
package com.github.lombrozo.jsmith.guard;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public final class SyntaxErrorListener implements ANTLRErrorListener {

    private final List<String> errors;

    public SyntaxErrorListener() {
        this(new ArrayList<>(0));
    }

    public SyntaxErrorListener(final List<String> errors) {
        this.errors = errors;
    }

    @Override
    public void syntaxError(
        final Recognizer<?, ?> recognizer,
        final Object offendingSymbol,
        final int line,
        final int charPositionInLine,
        final String msg, final RecognitionException e
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
        final BitSet ambigAlts,
        final ATNConfigSet configs
    ) {
        this.errors.add(String.format("Ambiguity from %s to %s", start, stop));
    }

    @Override
    public void reportAttemptingFullContext(
        final Parser recognizer,
        final DFA dfa,
        final int startIndex,
        final int stopIndex,
        final BitSet conflictingAlts,
        final ATNConfigSet configs
    ) {
        this.errors.add(
            String.format("Attempting full context from %s to %s", startIndex, stopIndex)
        );
    }

    @Override
    public void reportContextSensitivity(
        final Parser recognizer,
        final DFA dfa,
        final int startIndex,
        final int stopIndex,
        final int prediction,
        final ATNConfigSet configs
    ) {
        this.errors.add(
            String.format("Context sensitivity from %s to %s", startIndex, stopIndex)
        );
    }

    public void report() throws InvalidSyntax {
        if (!this.errors.isEmpty()) {
            throw new InvalidSyntax(this.errors.stream().collect(Collectors.joining(" ")));
        }
    }

}
