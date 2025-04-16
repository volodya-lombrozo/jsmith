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
 * Tests for {@link PredicateOption}.
 *
 * @since 0.2.0
 */
final class PredicateOptionTest {
    @Test
    void generatesPredicateOptionWithSingleElement() throws WrongPathException {
        final PredicateOption opt = new PredicateOption(new Root());
        opt.append(new ElementOption(opt, List.of(new Literal("test"))));
        MatcherAssert.assertThat(
            "We expect predicateOption to generate correctly",
            opt.generate(new Context()).text().output(),
            Matchers.equalTo("test")
        );
    }

    @Test
    void generatesPredicateOptionWithThreeElements() throws WrongPathException {
        final PredicateOption opt = new PredicateOption(new Root());
        final ActionBlock block = new ActionBlock(opt);
        block.append(new Literal("{ testBlock; }"));
        opt.append(new Identifier(opt, "testId"));
        opt.append(new Identifier(opt, "="));
        opt.append(block);
        MatcherAssert.assertThat(
            "We expect predicateOption to generate correctly",
            opt.generate(new Context()).text().output(),
            Matchers.equalTo("testId={ testBlock; }")
        );
    }

    @Test
    void throwsIllegalArgumentException() {
        final PredicateOption opt = new PredicateOption(new Root());
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> opt.append(new Literal("Wrong rule")),
            "We expect predicateOption to throw IllegalArgumentException"
        );
    }

    @Test
    void throwsIllegalStateException() {
        final PredicateOption opt = new PredicateOption(new Root());
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> opt.generate(new Context()),
            "We expect predicateOption to throw IllegalStateException"
        );
    }
}
