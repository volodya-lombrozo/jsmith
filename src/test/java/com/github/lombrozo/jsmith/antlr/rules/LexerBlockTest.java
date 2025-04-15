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
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LexerBlock}.
 *
 * @since 0.2.0
 */
final class LexerBlockTest {
    @Test
    void generatesLexerBlock() throws WrongPathException {
        final LexerBlock block = new LexerBlock();
        block.append(new Literal("LPAREN "));
        block.append(
            new LexerAltList(
                block,
                List.of(new Literal("1"), new Literal("2"))
            )
        );
        block.append(new Literal(" RPAREN"));
        MatcherAssert.assertThat(
            "We expect LexerBlock to generate correctly",
            block.generate(new Context()).text().output(),
            Matchers.oneOf("LPAREN 1 RPAREN", "LPAREN 2 RPAREN")
        );
    }

    @Test
    void throwsIllegalState() {
        final LexerBlock block = new LexerBlock();
        MatcherAssert.assertThat(
            "We expect message to be correct",
            Assertions.assertThrows(
            IllegalStateException.class,
            () -> block.generate(new Context()),
            "We expect IllegalStateException to be thrown"
        ).getMessage(),
            Matchers.equalTo("LexerBlock can't be empty")
        );
    }
}
