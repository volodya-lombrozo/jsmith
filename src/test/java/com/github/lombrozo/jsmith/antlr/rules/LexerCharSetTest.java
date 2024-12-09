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

import com.github.lombrozo.jsmith.antlr.Context;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test cases for {@link LexerCharSet}.
 * @since 0.1
 */
final class LexerCharSetTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "a",
        "b",
        "[a-c]",
        "[a-z]",
        "[a-zA-Z]",
        "[a-zA-Z0-9]",
        "[a-zA-Z0-9_]",
        "[\"\\\u0000-\u001F]",
        "[ \t\r\n]",
        "[ \\t\\r\\n]"
    })
    void generatesCharSequences(final String sequence) {
        MatcherAssert.assertThat(
            "We expect that the generated string will match the sequence",
            new LexerCharSet(sequence).generate(new Context()).text().output(),
            Matchers.matchesRegex(sequence)
        );
    }

    @Test
    void unescapesSequences() {
        MatcherAssert.assertThat(
            "We expect that ANTLR escape sequences will be unescaped",
            new LexerCharSet("[ \\t\\r\\n]").generate(new Context()).text().output(),
            Matchers.anyOf(
                Matchers.equalTo(" "),
                Matchers.equalTo("\t"),
                Matchers.equalTo("\r"),
                Matchers.equalTo("\n")
            )
        );
    }

    @Test
    void negatesControlCharacters() {
        final String sequence = "[\"\\\\\\u0000-\\u001F]";
        MatcherAssert.assertThat(
            "We expect that the generated string will not match the sequence",
            new LexerCharSet(sequence).negate(new Context()).output(),
            Matchers.not(Matchers.matchesRegex(sequence))
        );
    }
}
