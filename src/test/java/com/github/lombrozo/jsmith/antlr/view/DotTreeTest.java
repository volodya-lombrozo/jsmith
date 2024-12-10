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
import com.github.lombrozo.jsmith.antlr.rules.LexerRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.Literal;
import com.github.lombrozo.jsmith.antlr.rules.ParserRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
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
        MatcherAssert.assertThat(
            "We expect that the tree will be converted to DOT format",
            new DotTree(
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
            ).output(),
            Matchers.allOf(
                Matchers.containsString("JsmithGenerativeTree"),
                Matchers.containsString("literal(a) -> a"),
                Matchers.containsString("literal(b) -> b"),
                Matchers.containsString("literal(c) -> literal(d)"),
                Matchers.containsString("literal(c) -> literal(e)"),
                Matchers.containsString("literal(c) -> literal(a)"),
                Matchers.containsString("literal(f) -> literal(d)"),
                Matchers.containsString("literal(d) -> d")
            )
        );
    }

    @Test
    void createsSimpleTreeWithRulesOnly() {
        final Root top = new Root();
        final Rule first = new ParserRuleSpec("first", top);
        top.append(first);
        final Rule second = new ParserRuleSpec("second", first);
        first.append(second);
        final Rule third = new ParserRuleSpec("third", second);
        second.append(third);
        final Rule lexer = new LexerRuleSpec(third, "lexer");
        third.append(lexer);
        lexer.append(new Literal(lexer, "a"));
        MatcherAssert.assertThat(
            "We expect that only rules will be converted to DOT format",
            new DotTree(
                top.generate(new Context()).text(),
                new RulesOnly()
            ).output(),
            Matchers.equalTo(
                String.join(
                    "\n",
                    "digraph JsmithGenerativeTree{",
                    "// Node labels",
                    "\"#1678854096\" -> \"#1849201180\" [tooltip=\"root -> parserRuleSpec(first)\"];",
                    "\"#1849201180\" -> \"#513700442\" [tooltip=\"parserRuleSpec(first) -> parserRuleSpec(second)\"];",
                    "\"#513700442\" -> \"#366590980\" [tooltip=\"parserRuleSpec(second) -> parserRuleSpec(third)\"];",
                    "\"#1678854096\" [label=\"root\" tooltip=\"root\"];",
                    "\"#366590980\" [label=\"parserRuleSpec(third)\" tooltip=\"parserRuleSpec(third)\"];",
                    "\"#513700442\" [label=\"parserRuleSpec(second)\" tooltip=\"parserRuleSpec(second)\"];",
                    "\"#1849201180\" [label=\"parserRuleSpec(first)\" tooltip=\"parserRuleSpec(first)\"];",
                    " subgraph cluster_leafs {",
                    "rank=same",
                    "",
                    "  }",
                    "}"
                )
            )
        );
    }
}
