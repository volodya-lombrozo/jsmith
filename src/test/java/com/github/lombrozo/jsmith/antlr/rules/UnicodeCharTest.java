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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test cases for {@link UnicodeChar}.
 * @since 0.1
 */
final class UnicodeCharTest {

    @ParameterizedTest
    @CsvSource({
        "\\u0000, 0",
        "\\u0001, 1",
        "\\u270C, 9996",
        "\\u1234, 4660",
        "\\uFFFF, 65535",
    })
    void convertsToInt(final String origin, final int expected) {
        MatcherAssert.assertThat(
            "We expect that the character will be converted to a correct integer",
            new UnicodeChar(origin).chararcter(),
            Matchers.equalTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "\\u270A, '\u270A'",
        "\\u0001, '\u0001'",
        "\\u270C, '\u270C'",
        "\\u1234, '\u1234'",
        "\\uFFFF, '\uFFFF'",

    })
    void unescapes(final String origin, final String expected) {
        MatcherAssert.assertThat(
            "We expect that the character will be unescaped correctly",
            new UnicodeChar(origin).unescaped(),
            Matchers.equalTo(expected)
        );
    }
}
