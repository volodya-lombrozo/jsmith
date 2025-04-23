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
 * Tests for {@link OptionsSpec}.
 *
 * @since 0.2.0
 */
final class OptionsSpecTest {
    @Test
    void generatesOptionsSpec() throws WrongPathException {
        final OptionsSpec spec = new OptionsSpec(new Root());
        spec.append(new Literal("OPTIONS "));
        final Option option = new Option(spec);
        option.append(new Identifier(option, "id "));
        option.append(new Literal("ASSIGN"));
        option.append(new OptionValue(option, " optionValue "));
        spec.append(option);
        spec.append(new Literal("SEMI "));
        spec.append(new Literal("RBRACE"));
        MatcherAssert.assertThat(
            "We expect OptionSpec to generate correctly",
            spec.generate(new Context()).text().output(),
            Matchers.equalTo("OPTIONS id ASSIGN optionValue SEMI RBRACE")
        );
    }

    @Test
    void throwsIllegalArgument() {
        final OptionsSpec spec = new OptionsSpec(new Root());
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> spec.append(new Literal("Illegal rule")),
            "We expect IllegalArgumentException to be thrown"
        );
    }

    @Test
    void throwsIllegalState() {
        final OptionsSpec spec = new OptionsSpec(new Root());
        MatcherAssert.assertThat(
            "We expect correct error message to be provided",
            Assertions.assertThrows(
                IllegalStateException.class,
                () -> spec.generate(new Context())
            ).getMessage(),
            Matchers.equalTo("OptionsSpec must have at least 2 elements, provided 0")
        );
    }
}
