package com.github.lombrozo.jsmith.guard;

import java.nio.file.Path;
import org.cactoos.Input;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link SyntaxGuard}.
 */
final class SyntaxGuardTest {

    /**
     * Grammar file.
     */
    private static final Input GRAMMAR = new ResourceOf("grammars/Simple.g4");

    @Test
    void validatesCorrectSynax(@TempDir final Path temp) {
        Assertions.assertDoesNotThrow(
            () -> new SyntaxGuard(temp, SyntaxGuardTest.GRAMMAR).verify("1 + 1"),
            "We expect that the code will be verified without errors"
        );
    }

    @Test
    void throwsInvalidSyntaxOnIncorrectSyntax(@TempDir final Path temp) {
        MatcherAssert.assertThat(
            "We expect that the code will be verified with errors and error message will be informative",
            Assertions.assertThrows(
                InvalidSyntax.class,
                () -> new SyntaxGuard(temp, SyntaxGuardTest.GRAMMAR).verify("1 - 1"),
                "We expect that the code will be verified with errors"
            ).getMessage(),
            Matchers.equalTo("Incorrect syntax")
        );
    }

    @Test
    void throwsInvalidSyntaxOnEmptyCode(@TempDir final Path temp) {
        MatcherAssert.assertThat(
            "We expect that the empty code will be verified with errors and error message will be informative",
            Assertions.assertThrows(
                InvalidSyntax.class,
                () -> new SyntaxGuard(temp, SyntaxGuardTest.GRAMMAR).verify(""),
                "We expect that the empty code will be verified with errors"
            ).getMessage(),
            Matchers.equalTo("Empty code")
        );
    }
}