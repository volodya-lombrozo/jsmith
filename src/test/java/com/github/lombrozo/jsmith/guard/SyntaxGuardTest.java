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

import java.nio.file.Path;
import org.cactoos.Input;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {
        "grammar Simple;",
        "lexer grammar Simple;",
        "parser grammar Simple;",
        "grammar Simple; expr: NUMBER;"
    })
    void findsGrammarName(final String grammar) {
        MatcherAssert.assertThat(
            "We expect that the grammar name will be found",
            SyntaxGuard.grammarName(grammar),
            Matchers.equalTo("Simple")
        );
    }
}