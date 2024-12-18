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

import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test case for {@link AntlrString}.
 * @since 0.1
 */
final class AntlrStringTest {

    @ParameterizedTest
    @MethodSource("apostrophes")
    void removesApostrophes(final String input, final String expected) {
        MatcherAssert.assertThat(
            "We expect that the AntlrString will replace escape sequences",
            new AntlrString(input).asString(),
            Matchers.equalTo(expected)
        );
    }

    /**
     * Test data for the test {@link #removesApostrophes(String, String)}.
     * @return Test data.
     */
    static Stream<Arguments> apostrophes() {
        return Stream.of(
            Arguments.of("'Hello, world!'", "Hello, world!"),
            Arguments.of("''world!''", "'world!'"),
            Arguments.of("''''", "''"),
            Arguments.of("Java\'jin", "Java'jin"),
            Arguments.of("Java\'jin\'", "Java'jin'"),
            Arguments.of("\'Java\'jin\'", "'Java'jin'")
        );
    }

}