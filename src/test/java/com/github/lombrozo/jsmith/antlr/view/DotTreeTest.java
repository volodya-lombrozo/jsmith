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
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link DotTree}.
 * @since 0.1
 */
final class DotTreeTest {

    @Test
    void convertsSimpleTree() {
        final TextNode tree = new TextNode(
            new Root(),
            Arrays.asList(
                new TextLeaf(new Literal("a"), "a"),
                new TextLeaf(new Literal("b"), "b"),
                new TextNode(
                    new Literal("c"),
                    Arrays.asList(
                        new TextLeaf(new Literal("d"), "d"),
                        new TextLeaf(new Literal("e"), "e")
                    )
                )
            )
        );
        //todo: good assertions are needed!
        final String output = new DotTree(tree).output();
        System.out.println(output);
    }

}