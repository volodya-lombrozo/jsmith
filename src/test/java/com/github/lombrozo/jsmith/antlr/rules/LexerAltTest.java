/*
 * MIT License
 *
 * Copyright (c) 2023-2025 Volodya Lombrozo
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LexerAlt}.
 *
 * @since 0.2.0
 */
final class LexerAltTest {
    @Test
    void generatesEmptyAlt() throws WrongPathException {
        final LexerAlt alt = new LexerAlt(new Root());
        MatcherAssert.assertThat(
            "We expect LexerAlt to generate empty alternative",
            alt.generate(new Context()).text().output(),
            Matchers.equalTo("")
        );
    }

    @Test
    void generatesAlt() throws WrongPathException {
        final LexerAlt alt = new LexerAlt(new Root());
        final LexerElements elements = new LexerElements(alt);
        final LexerElement elem = new LexerElement(elements);
        elem.append(new Literal("a"));
        elem.append(new EbnfSuffix("?"));
        elements.append(elem);
        alt.append(elements);
        MatcherAssert.assertThat(
            "We expect LexerAlt to generate correctly",
            alt.generate(new Context()).text().output(),
            Matchers.anyOf(
                Matchers.emptyString(),
                Matchers.equalTo("a")
            )
        );
    }

    @Test
    void throwsIllegalArgument() {
        final LexerAlt alt = new LexerAlt(new Root());
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> alt.append(new Literal("wrong rule")),
            "We expect IllegalArgumentException to be thrown"
        );
    }
}
