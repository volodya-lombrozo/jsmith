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
 * Tests for {@link LabeledElement}.
 *
 * @since 0.2.0
 */
final class LabeledElementTest {
    @Test
    void generatesLabeledElement() throws WrongPathException {
        final LabeledElement element = new LabeledElement(new Root());
        element.append(new Identifier(element, "test "));
        element.append(new Literal("ASSIGN"));
        final Block block = new Block(element);
        block.append(new Literal(" test2"));
        element.append(block);
        MatcherAssert.assertThat(
            "We expect LabeledElement to generate",
            element.generate(new Context()).text().output(),
            Matchers.equalTo("test ASSIGN test2")
        );
    }

    @Test
    void throwsIllegalArgument() {
        final LabeledElement element = new LabeledElement(new Root());
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> element.append(new Literal("Illegal rule")),
            "We expect LabeledElement to throw an IllegalArgument"
        );
    }

    @Test
    void throwsIllegalState() {
        final LabeledElement element = new LabeledElement(new Root());
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> element.generate(new Context()),
            "We expect element to throw an IllegalState trying to generate without children"
        );
    }
}
