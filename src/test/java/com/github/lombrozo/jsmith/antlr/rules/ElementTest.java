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
 * Test cases for {@link Element}.
 *
 * @since 0.1
 */
final class ElementTest {

    @Test
    void generatesUsingAtomBranch() throws WrongPathException {
        final Root root = new Root();
        final Rule element = new Element(root);
        final Rule atom = new Atom(root);
        final String number = "1";
        atom.append(new Literal(number));
        element.append(atom);
        MatcherAssert.assertThat(
            "We expect that the element with an atom will be generated correctly",
            element.generate(new Context()).text().output(),
            Matchers.equalTo(number)
        );
    }

    @Test
    void generatesUsingAtomBranchAndEbnfSuffix() throws WrongPathException {
        final Root root = new Root();
        final Rule element = new Element(root);
        final Rule atom = new Atom(root);
        atom.append(new Literal("1"));
        element.append(atom);
        element.append(new EbnfSuffix("*"));
        MatcherAssert.assertThat(
            "We expect that the element with EBNF suffix will be generated correctly",
            element.generate(new Context()).text().output(),
            Matchers.anyOf(
                Matchers.equalTo(""),
                Matchers.equalTo("1"),
                Matchers.containsString("11")
            )
        );
    }
}
