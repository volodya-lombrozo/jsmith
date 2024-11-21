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
package com.github.lombrozo.jsmith.random;

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.rules.Literal;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link Multiplier}.
 * @since 0.1
 */
final class MultiplierTest {

    @Test
    void generatesZeroOrMore() {
        MatcherAssert.assertThat(
            "We expect that the 'zero or more' multiplier will generate zero or more elements",
            new Multiplier.ZeroOrMore()
                .repeat(new Literal("a"))
                .generate(new Context())
                .output(),
            Matchers.anyOf(
                Matchers.emptyString(),
                Matchers.equalTo("a"),
                Matchers.containsString("aa")
            )
        );
    }

    @Test
    void generatesOneOrMore() {
        MatcherAssert.assertThat(
            "We expect that the 'one or more' multiplier will generate one or more elements",
            new Multiplier.OneOrMore()
                .repeat(new Literal("a"))
                .generate(new Context())
                .output(),
            Matchers.anyOf(
                Matchers.equalTo("a"),
                Matchers.containsString("aa")
            )
        );
    }

    @Test
    void generatesZeroOrOne() {
        MatcherAssert.assertThat(
            "We expect that the 'zero or one' multiplier will generate zero or one element",
            new Multiplier.ZeroOrOne()
                .repeat(new Literal("a"))
                .generate(new Context()).output(),
            Matchers.anyOf(
                Matchers.emptyString(),
                Matchers.equalTo("a")
            )
        );
    }

    @Test
    void generatesExactlyOne() {
        MatcherAssert.assertThat(
            "We expect that the 'exactly one' multiplier will generate exactly one element",
            new Multiplier.One().repeat(new Literal("a")).generate(new Context()).output(),
            Matchers.equalTo("a")
        );
    }
}
