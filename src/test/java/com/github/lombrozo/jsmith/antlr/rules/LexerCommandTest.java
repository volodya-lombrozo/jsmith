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
 * Tests for {@link LexerCommand}.
 *
 * @since 0.2.0
 */
final class LexerCommandTest {
    @Test
    void generatesLexerCommand() throws WrongPathException {
        final LexerCommand cmd = new LexerCommand(new Root());
        final LexerCommandName name = new LexerCommandName(cmd);
        name.append(new Identifier(name, "testname"));
        cmd.append(name);
        MatcherAssert.assertThat(
            "We expect LexerCommand to generate with LexerCommandName",
            cmd.generate(new Context()).text().output(),
            Matchers.equalTo("testname")
        );
    }

    @Test
    void throwsIllegalArgument() {
        final LexerCommand cmd = new LexerCommand(new Root());
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> cmd.append(new Identifier(cmd, "test")),
            "We expect LexerCommand to throw exception if wrong rule is being added"
        );
    }

    @Test
    void throwsIllegalState() {
        final LexerCommand cmd = new LexerCommand(new Root());
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> cmd.generate(new Context()),
            "We expect IllegalStateException if lexercommand elements is empty"
        );
    }
}
