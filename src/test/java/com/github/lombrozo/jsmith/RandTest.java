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
package com.github.lombrozo.jsmith;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link Rand}.
 * @since 0.1
 */
class RandTest {

    @ParameterizedTest
    @ValueSource(strings = {"[0-3]+", "[0-3]"})
    void regexGenerationTest(final String regex) {
        final Rand rand = new Rand();
        final String actual = rand.regex(regex);
        MatcherAssert.assertThat(
            "We expect that the generated string will match the regex pattern",
            actual,
            Matchers.matchesRegex(regex)
        );
    }

    @Test
    void checksRandomizer() {
        MatcherAssert.assertThat(
            "We expect that the randomizer will generate a number from 0 to 4",
            new Rand().nextInt(5),
            Matchers.anyOf(
                Matchers.equalTo(0),
                Matchers.equalTo(1),
                Matchers.equalTo(2),
                Matchers.equalTo(3),
                Matchers.equalTo(4)
            )
        );
    }
}