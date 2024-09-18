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
package com.github.lombrozo.jsmith.antlr.view;

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.rules.AltList;
import com.github.lombrozo.jsmith.antlr.rules.Atom;
import com.github.lombrozo.jsmith.antlr.rules.LexerAtom;
import com.github.lombrozo.jsmith.antlr.rules.LexerBlock;
import com.github.lombrozo.jsmith.antlr.rules.Literal;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import com.github.lombrozo.jsmith.antlr.rules.RuleAltList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link TextTree}.
 * @since 0.1
 */
final class TextTreeTest {

    @Test
    void outputsSimpleTree() {
        final Root root = new Root();
        final LexerAtom atom = new LexerAtom(root);
        atom.append(new Literal(atom, "a"));
        final LexerBlock block = new LexerBlock(root);
        block.append(new Literal(block, "b"));
        root.append(atom);
        root.append(block);
        final Text generated = root.generate(new Context());
        MatcherAssert.assertThat(
            "We expect that the text tree will be printed correctly",
            new TextTree(generated).output(),
            Matchers.equalTo(
                String.join(
                    "\n",
                    "root\tlexerAtom\tliteral(a): 'a'",
                    "\t\tlexerBlock\tliteral(b): 'b'"
                )
            )
        );
    }

    @Test
    void outputsComplexTreeWithDifferentLengths() {
        final Root root = new Root();
        final TextNode text = new TextNode(
            root,
            new TextLeaf(new RuleAltList(root), "a"),
            new TextNode(new Atom(root),
                new TextLeaf(root, "b1"),
                new TextLeaf(root, "b2")),
            new TextLeaf(root, "c")
        );
        System.out.println(new TextTree(text).output());
    }
}