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
package com.github.lombrozo.jsmith.antlr;

import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link EbnfSuffix}.
 *
 * @since 0.1
 */
final class EbnfSuffixTest {

    /**
     * Generates all possible combinations of the EBNF suffix.
     * @return Stream of arguments for {@link #generatesSimpleEbnfSuffix(String, String, String)} ()}
     */
    public static Stream<Arguments> combinations() {
        return Stream.of(
            Arguments.of("?", null, "?"),
            Arguments.of("?", "?", "??"),
            Arguments.of("*", null, "*"),
            Arguments.of("*", "?", "*?"),
            Arguments.of("+", null, "+"),
            Arguments.of("+", "?", "+?")
        );
    }

    @ParameterizedTest
    @MethodSource("combinations")
    void generatesSimpleEbnfSuffix(
        final String operation, final String question, final String expected
    ) {
        MatcherAssert.assertThat(
            "We expect that the EBNF suffix will be generated",
            new EbnfSuffix(operation, question).generate(),
            Matchers.equalTo(expected)
        );
    }

    @Test
    void throwsExceptionWhenOperationIsNull() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new EbnfSuffix(null).generate(),
            "We expect that an exception will be thrown when the operation is null"
        );
    }

}