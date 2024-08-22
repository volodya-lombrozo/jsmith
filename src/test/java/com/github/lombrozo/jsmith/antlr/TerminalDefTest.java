package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Unparser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * TerminalDef test.
 * Test cases for {@link TerminalDef}
 * @since 0.1
 * @todo #1:30min Implement TerminalDefTest!
 *  We need at least some test cases for TerminalDef.
 *  After adding the tests, refactor the {@link TerminalDef} class itself.
 */
final class TerminalDefTest {

    @ParameterizedTest
    @ValueSource(strings = {"'+'", "'\r'"})
    void generatesCorrectSymbols(final String initial) {
        System.out.println(
            new TerminalDef(
                new Root(), new Unparser(), initial
            ).generate()
        );
        throw new UnsupportedOperationException("Not implemented yet");
    }

}