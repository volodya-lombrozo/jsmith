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
package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.antlr.rules.AltList;
import com.github.lombrozo.jsmith.antlr.rules.Literal;
import com.github.lombrozo.jsmith.antlr.rules.RuleDefinition;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Сonvergence}.
 * @since 0.1
 */
final class СonvergenceTest {


    @Test
    void choosesDesiredElementEvenIfTheInitalProbabilityIsLow() {
        final AltList list = new AltList();
        final List<AltList> recursive = Collections.nCopies(100, list);
        final Literal desired = new Literal("desired");
        final RuleDefinition[] args = Stream.concat(
            recursive.stream(),
            Stream.of(desired)
        ).toArray(RuleDefinition[]::new);
        list.append(desired);
        final Сonvergence<RuleDefinition> convergence = new Сonvergence(0.01);
        convergence.choose(list, args);
        convergence.choose(list, args);
        MatcherAssert.assertThat(
            "",
            convergence.choose(list, args),
            Matchers.equalTo(desired)
        );
    }

}