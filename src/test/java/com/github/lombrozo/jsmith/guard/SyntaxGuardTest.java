package com.github.lombrozo.jsmith.guard;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SyntaxGuard}.
 */
final class SyntaxGuardTest {

    private static final String GRAMMAR = "grammars/Simple.g4";

    @Test
    void validatesCorrectSynax() {
        Assertions.assertDoesNotThrow(
            () -> new SyntaxGuard(SyntaxGuardTest.GRAMMAR).verify("1 + 1"),
            "We expect that the code will be verified without errors"
        );
    }

    @Test
    void throwsInvalidSyntaxOnIncorrectSyntax() {
        MatcherAssert.assertThat(
            "We expect that the code will be verified with errors and error message will be informative",
            Assertions.assertThrows(
                InvalidSyntax.class,
                () -> new SyntaxGuard(SyntaxGuardTest.GRAMMAR).verify("1 - 1"),
                "We expect that the code will be verified with errors"
            ).getMessage(),
            Matchers.equalTo("Incorrect syntax")
        );
    }

    @Test
    void throwsInvalidSyntaxOnEmptyCode() {
        MatcherAssert.assertThat(
            "We expect that the empty code will be verified with errors and error message will be informative",
            Assertions.assertThrows(
                InvalidSyntax.class,
                () -> new SyntaxGuard(SyntaxGuardTest.GRAMMAR).verify(""),
                "We expect that the empty code will be verified with errors"
            ).getMessage(),
            Matchers.equalTo("Empty code")
        );
    }
}