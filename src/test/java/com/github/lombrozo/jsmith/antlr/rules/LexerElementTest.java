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

import com.github.lombrozo.jsmith.antlr.Context;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link LexerElement}.
 * @since 0.1
 */
final class LexerElementTest {

    @Test
    void generatesLexerAtomWithEbnfSuffix() {
        final Rule element = new LexerElement();
        final Rule atom = new LexerAtom();
        final Rule literal = new Literal("a");
        atom.append(literal);
        element.append(atom);
        element.append(new EbnfSuffix("?"));
        MatcherAssert.assertThat(
            "We expect that the atom element will be printed correctly with the correct number of repetitions",
            element.generate(new Context()).text().output(),
            Matchers.anyOf(
                Matchers.emptyString(),
                Matchers.equalTo("a")
            )
        );
    }

    @Test
    void generatesLexerAtomWithoutEbnfSuffix() {
        final LexerElement element = new LexerElement();
        final Rule atom = new LexerAtom(element);
        final Rule literal = new Literal("a");
        atom.append(literal);
        element.append(atom);
        MatcherAssert.assertThat(
            "We expect that the atom element will be printed correctly without the number of repetitions",
            element.generate(new Context()).text().output(),
            Matchers.equalTo("a")
        );
    }

    @Test
    void generatesLexerBlockWithEbnfSuffix() {
        final Rule element = new LexerElement();
        final Rule block = new LexerBlock();
        final Rule literal = new Literal("b");
        block.append(literal);
        element.append(block);
        element.append(new EbnfSuffix("+"));
        MatcherAssert.assertThat(
            "We expect that the lexer block will be printed correctly with the correct number of repetitions",
            element.generate(new Context()).text().output(),
            Matchers.anyOf(
                Matchers.equalTo("b"),
                Matchers.containsString("bb")
            )
        );
    }

    @Test
    void generatesLexerBlockWithoutEbnfSuffix() {
        final Rule element = new LexerElement();
        final Rule block = new LexerBlock();
        final Rule literal = new Literal("b");
        block.append(literal);
        element.append(block);
        MatcherAssert.assertThat(
            "We expect that the lexer block will be printed exactly once",
            element.generate(new Context()).text().output(),
            Matchers.equalTo("b")
        );
    }

    @Test
    void generatesActionBlockWithoutQuestion() {
        final Rule element = new LexerElement();
        final Rule action = new ActionBlock();
        final Rule literal = new Literal("c");
        action.append(literal);
        element.append(action);
        MatcherAssert.assertThat(
            "We expect that the action block will be printed exactly once",
            element.generate(new Context()).text().output(),
            Matchers.equalTo("c")
        );
    }

    @Test
    void generatesActionBlockWithQuestion() {
        final Rule element = new LexerElement();
        final Rule action = new ActionBlock();
        final Rule literal = new Literal("c");
        action.append(literal);
        element.append(action);
        element.append(new EbnfSuffix("?"));
        MatcherAssert.assertThat(
            "We expect that the action block will be printed once or not at all",
            element.generate(new Context()).text().output(),
            Matchers.anyOf(
                Matchers.emptyString(),
                Matchers.equalTo("c")
            )
        );
    }
}
