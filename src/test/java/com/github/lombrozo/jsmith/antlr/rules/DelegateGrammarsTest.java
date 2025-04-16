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
 * Tests for {@link DelegateGrammars}.
 *
 * @since 0.2.0
 */
final class DelegateGrammarsTest {
    @Test
    void generatesDelegateGrammars() throws WrongPathException {
        final DelegateGrammars grammars = new DelegateGrammars(new Root());
        grammars.append(new Literal("IMPORT "));
        final DelegateGrammar delgram = new DelegateGrammar(grammars);
        delgram.append(new Identifier(delgram, "testGrammar"));
        grammars.append(delgram);
        grammars.append(new Literal(" SEMI"));
        MatcherAssert.assertThat(
            "We expect delegateGrammars to generate correctly",
            grammars.generate(new Context()).text().output(),
            Matchers.equalTo("IMPORT testGrammar SEMI")
        );
    }

    @Test
    void throwsIllegalArgument() {
        final DelegateGrammars grammars = new DelegateGrammars(new Root());
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> grammars.append(new Literal("Wrong rule")),
            "We expect IllegalArgumentException to be thrown"
        );
    }

    @Test
    void throwsIllegalState() {
        final DelegateGrammars grammars = new DelegateGrammars(new Root());
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> grammars.generate(new Context()),
            "We expect IllegalStateException to be thrown"
        );
    }
}
