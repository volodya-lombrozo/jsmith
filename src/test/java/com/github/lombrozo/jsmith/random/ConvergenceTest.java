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
package com.github.lombrozo.jsmith.random;

import com.github.lombrozo.jsmith.antlr.rules.AltList;
import com.github.lombrozo.jsmith.antlr.rules.Literal;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Convergence}.
 * @since 0.1
 */
final class ConvergenceTest {

    @Test
    void choosesDesiredElementEvenIfTheInitalProbabilityIsLow() {
        final AltList list = new AltList();
        final Literal desired = new Literal("desired");
        final Rule[] args = Stream.concat(
            Collections.nCopies(100_000, list).stream(),
            Stream.of(desired)
        ).toArray(Rule[]::new);
        final Convergence<Rule> convergence = new Convergence<>(0.000_000_000_1, true);
        convergence.choose(list, args);
        MatcherAssert.assertThat(
            String.format(
                "We expect that in the first round, the '%s' element will be chosen. However, the probability of choosing it will decrease significantly, and it won't be chosen in the second round.",
                list
            ),
            convergence.choose(list, args),
            Matchers.equalTo(desired)
        );
    }

    @RepeatedTest(10)
    void correctChoosingDistribution() {
        final Convergence<Rule> convergence = new Convergence<>(0.5, false);
        final Rule root = new Root();
        final Rule a = new Literal("a");
        final Rule b = new Literal("b");
        final Rule c = new Literal("c");
        final Map<Rule, Integer> frequency = new HashMap<>(0);
        for (int index = 0; index < 1000; ++index) {
            frequency.compute(
                convergence.choose(root, a, b, c),
                (key, value) -> value == null ? 1 : value + 1
            );
        }
        MatcherAssert.assertThat(
            "We expect that all elements were chosen more-or-less equally.",
            frequency.values(),
            Matchers.everyItem(Matchers.greaterThan(300))
        );
    }

    @RepeatedTest(10)
    void choosesAllElements() {
        final Rule root = new AltList();
        final List<Rule> alternatives = IntStream.range(0, 5)
            .mapToObj(String::valueOf)
            .map(Literal::new)
            .peek(root::append)
            .collect(Collectors.toList());
        final Convergence<Rule> convergence = new Convergence<>(0.5, false);
        MatcherAssert.assertThat(
            "We expect that all elements were chosen at least once.",
            IntStream.range(0, 50)
                .mapToObj(i -> convergence.choose(root, alternatives))
                .collect(Collectors.toSet()),
            Matchers.containsInAnyOrder(alternatives.toArray())
        );
    }
}
