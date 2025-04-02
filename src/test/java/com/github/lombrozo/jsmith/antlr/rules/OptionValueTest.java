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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OptionValue}.
 *
 * @since 0.2.x
 */
final class OptionValueTest {
    @Test
    void generatesOptionValueWithIdentifiers() throws WrongPathException {
        final OptionValue option = new OptionValue(new Root());
        option.append(new Identifier(option, "TestId"));
        option.append(new Identifier(option, "TestId2"));
        Assertions.assertEquals(
            option.generate(new Context()).text().output(),
            "TestId.TestId2",
            "We expect OptionValue to generate chain of identifiers with dots in between."
        );
    }

    @Test
    void generatesWithRule() throws WrongPathException {
        final Identifier id = new Identifier(new Root(), "TestId");
        final OptionValue option = new OptionValue(new Root(), id);
        Assertions.assertEquals(
            id.generate(new Context()).text().output(),
            option.generate(new Context()).text().output(),
            "We expect optionValue to generate correctly with single rule"
        );
    }

    @Test
    void generatesWithString() throws WrongPathException {
        final String ref = "Test";
        final OptionValue option = new OptionValue(new Root(), ref);
        Assertions.assertEquals(
            ref,
            option.generate(new Context()).text().output(),
            "We expect optionValue to generate correctly with string"
        );
    }

    @Test
    void generatesWithInt() throws WrongPathException {
        final OptionValue option = new OptionValue(new Root(), 10);
        Assertions.assertEquals(
            "10",
            option.generate(new Context()).text().output(),
            "We expect optionValue to generate correctly with integer"
        );
    }

    @Test
    void throwsIllegalStateException() throws WrongPathException {
        final OptionValue option = new OptionValue(new Root());
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> option.generate(new Context()),
            "We expect empty OptionValue to throw IllegalStateException"
        );
    }
}
