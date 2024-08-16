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

    /**
     * Top rule.
     */
    private static final String TOP = "expr";

    @Test
    void validatesCorrectSynax(@TempDir final Path temp) {
        Assertions.assertDoesNotThrow(
            () -> new SyntaxGuard(temp, SyntaxGuardTest.GRAMMAR, SyntaxGuardTest.TOP)
                .verify("1 + 1"),
            "We expect that the code will be verified without errors"
        );
    }

    @Test
    void throwsInvalidSyntaxOnIncorrectSyntax(@TempDir final Path temp) {
        MatcherAssert.assertThat(
            "We expect that the code will be verified with errors and error message will be informative",
            Assertions.assertThrows(
                InvalidSyntax.class,
                () -> new SyntaxGuard(temp, SyntaxGuardTest.GRAMMAR, SyntaxGuardTest.TOP)
                    .verify("1 - 1"),
                "We expect that the code will be verified with errors"
            ).getMessage(),
            Matchers.equalTo("token recognition error at: '-'")
        );
    }

    @Test
    void throwsInvalidSyntaxOnEmptyCode(@TempDir final Path temp) {
        MatcherAssert.assertThat(
            "We expect that the empty code will be verified with errors and error message will be informative",
            Assertions.assertThrows(
                InvalidSyntax.class,
                () -> new SyntaxGuard(temp, SyntaxGuardTest.GRAMMAR, SyntaxGuardTest.TOP)
                    .verify(""),
                "We expect that the empty code will be verified with errors"
            ).getMessage(),
            Matchers.equalTo("missing NUMBER at '<EOF>'")
        );
    }
}