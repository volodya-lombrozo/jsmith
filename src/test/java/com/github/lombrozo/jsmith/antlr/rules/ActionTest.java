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
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Action}.
 *
 * @since 0.2.x
 */
final class ActionTest {
    @Test
    void generatesEmptyAction() throws WrongPathException {
        final Action action = new Action(new Root());
        action.append(new Literal("@"));
        action.append(new Identifier(action));
        action.append(new ActionBlock(action));
        MatcherAssert.assertThat(
            "We expect it to generate string with @ character",
            action.generate(new Context()).text().output(),
            Matchers.equalTo("@")
        );
    }

    @Test
    void generatesAction() throws WrongPathException {
        final Action action = new Action(new Root());
        action.append(new Literal("@"));
        action.append(new ActionScopeName(action, "testScope"));
        action.append(new Literal("::"));
        action.append(new Identifier(action, "testId"));
        action.append(
            new ActionBlock(action, List.of(new Literal("{Test Block;}")))
        );
        MatcherAssert.assertThat(
            "We expect this action to generate with all elements",
            action.generate(new Context()).text().output(),
            Matchers.equalTo("@testScope::testId{Test Block;}")
        );
    }

    @Test
    void throwsIllegalStateException() throws WrongPathException {
        final Action action = new Action(new Root());
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> action.generate(new Context()),
            "We expect action class to throw IllegalStateException if action is created without necessary elements"
        );
    }
}
