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

import com.github.lombrozo.jsmith.antlr.Unlexer;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link TerminalDef}.
 * @since 0.1
 */
final class TerminalDefTest {

    @Test
    void retrievesLexerRule() {
        final Unlexer unlexer = new Unlexer();
        final String name = "PLUS";
        final LexerRuleSpec rule = new LexerRuleSpec(name);
        final String text = "+";
        rule.append(new Literal(text));
        unlexer.with(name, rule);
        MatcherAssert.assertThat(
            "We expect that the lexer rule will be retrieved and this rule will generate the text",
            new TerminalDef(unlexer, name).generate(),
            Matchers.equalTo(text)
        );
    }

    @Test
    void retrievesLiteral() {
        final String text = "-";
        MatcherAssert.assertThat(
            "We expect that the literal will be retrieved",
            new TerminalDef(new Unlexer(), text).generate(),
            Matchers.equalTo(text)
        );
    }
}