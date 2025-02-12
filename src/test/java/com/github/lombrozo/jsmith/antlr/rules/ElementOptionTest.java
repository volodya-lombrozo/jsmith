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
 * Tests for {@link ElementOption}.
 *
 * @since 0.2.0
 */
final class ElementOptionTest {
    @Test
    void generatesElementOption() throws WrongPathException {
        final ElementOption rule = new ElementOption(new Root());
        rule.append(new Identifier(rule, "test"));
        rule.append(new Literal("="));
        rule.append(new Identifier(rule, "test;"));
        MatcherAssert.assertThat(
            "We expect ElementOption to generate properly",
            rule.generate(new Context()).text().output(),
            Matchers.equalTo("test=test;")
        );
    }

    @Test
    void throwsIllegalStateException() {
        final ElementOption rule = new ElementOption(new Root());
        Assertions.assertThrows(
            IllegalStateException.class, () -> rule.generate(new Context()),
            "We expect it to throw IllegalStateException"
        );
    }
}
