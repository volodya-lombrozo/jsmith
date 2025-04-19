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
 * Tests for {@link Option}.
 *
 * @since 0.2.0
 */
final class OptionTest {
    @Test
    void generatesOption() throws WrongPathException {
        final Option option = new Option(new Root());
        option.append(new Identifier(option, "id "));
        option.append(new Literal("ASSIGN"));
        option.append(new OptionValue(option, " optionValue"));
        MatcherAssert.assertThat(
            "We expect option to generate correctly",
            option.generate(new Context()).text().output(),
            Matchers.equalTo("id ASSIGN optionValue")
        );
    }

    @Test
    void throwsIllegalArgument() {
        final Option option = new Option(new Root());
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> option.append(new Action(option)),
            "We expect an exception to be thrown because of unsupported rule appending to Option class"
        );
    }

    @Test
    void throwsIllegalState() {
        final Option option = new Option(new Root());
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> option.generate(new Context()),
            "We expect an exception to be thrown because of empty Option trying to be generated"
        );
    }
}
