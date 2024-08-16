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
