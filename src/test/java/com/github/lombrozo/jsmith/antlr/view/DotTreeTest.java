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

import com.github.lombrozo.jsmith.antlr.rules.Literal;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import java.util.Arrays;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link DotTree}.
 * @since 0.1
 */
final class DotTreeTest {

    @Test
    void convertsSimpleTree() {
        final String output = new DotTree(
            new TextNode(
                new Root(),
                Arrays.asList(
                    new TextLeaf(new Literal("a"), "a"),
                    new TextLeaf(new Literal("b"), "b"),
                    new TextNode(
                        new Literal("c"),
                        Arrays.asList(
                            new TextLeaf(new Literal("d"), "d"),
                            new TextLeaf(new Literal("e"), "e"),
                            new TextLeaf(new Literal("a"), "a")
                        )
                    ),
                    new TextNode(
                        new Literal("f"),
                        Collections.singletonList(
                            new TextLeaf(new Literal("d"), "d")
                        )
                    )
                )
            )
        ).output();
        System.out.println(output);
        MatcherAssert.assertThat(
            output,
            Matchers.equalTo(
                String.join(
                    "\n",
                    "digraph JsmithGenerativeTree{",
                    "\"#1558021762\" -> \"#225290371\";",
                    "\"#225290371\" -> \"#733672688\";",
                    "\"#225290371\" -> \"#297927961\";",
                    "\"#225290371\" -> \"#1891546521\";",
                    "\"#1891546521\" -> \"#1312884893\";",
                    "\"#1891546521\" -> \"#849373393\";",
                    "\"#1891546521\" -> \"#868964689\";",
                    "\"#225290371\" -> \"#912011468\";",
                    "\"#912011468\" -> \"#1881129850\";",
                    "// Node labels",
                    "\"#297927961\" [label=\"literal(b)\"];",
                    "\"#868964689\" [label=\"literal(a)\"];",
                    "\"#912011468\" [label=\"literal(f)\"];",
                    "\"#849373393\" [label=\"literal(e)\"];",
                    "\"#225290371\" [label=\"root\"];",
                    "\"#1881129850\" [label=\"literal(d)\"];",
                    "\"#733672688\" [label=\"literal(a)\"];",
                    "\"#1558021762\" [label=\"root\"];",
                    "\"#1891546521\" [label=\"literal(c)\"];",
                    "\"#1312884893\" [label=\"literal(d)\"];",
                    "}"
                )
            )
        );
    }
}