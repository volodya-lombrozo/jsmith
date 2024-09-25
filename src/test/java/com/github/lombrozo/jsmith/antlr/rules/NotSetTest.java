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
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for {@link NotSet}.
 * @since 0.1
 */
final class NotSetTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "[<\"]*", "[<']*", "[\"\\\u0000-\u001F]"})
    void negatesLexerCharSet(final String sequence) {
        final String negated;
        if (sequence.startsWith("[")) {
            negated = sequence.substring(0, 1) + "^" + sequence.substring(1);
        } else {
            negated = "[^" + sequence + "]";
        }
        final Root roo = new Root();
        final NotSet not = new NotSet(roo);
        final SetElement element = new SetElement(not);
        element.append(new LexerCharSet(negated));
        not.append(element);
        final String output = not.generate().output();
        System.out.println(output);
        MatcherAssert.assertThat(
            "We expect that the generated string will not match the sequence",
            output,
            Matchers.not(Matchers.matchesRegex(sequence))
        );
    }
}
