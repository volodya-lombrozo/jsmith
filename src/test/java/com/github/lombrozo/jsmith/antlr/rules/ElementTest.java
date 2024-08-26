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

import com.github.lombrozo.jsmith.antlr.rules.Atom;
import com.github.lombrozo.jsmith.antlr.rules.EbnfSuffix;
import com.github.lombrozo.jsmith.antlr.rules.Element;
import com.github.lombrozo.jsmith.antlr.rules.Literal;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import com.github.lombrozo.jsmith.antlr.rules.RuleDefinition;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link Element}.
 *
 * @since 0.1
 */
final class ElementTest {

    @Test
    void generatesUsingAtomBranch() {
        final Root root = new Root();
        final RuleDefinition element = new Element(root);
        final Atom atom = new Atom(root);
        atom.append(new Literal("1"));
        element.append(atom);
        //todo: assert
        System.out.println(element.generate());
    }

    @Test
    void generatesUsingAtomBranchAndEbnfSuffix() {
        final Root root = new Root();
        final RuleDefinition element = new Element(root);
        final Atom atom = new Atom(root);
        atom.append(new Literal("1"));
        element.append(atom);
        element.append(new EbnfSuffix("*"));
        //todo: assert
        System.out.println(element.generate());
    }
}