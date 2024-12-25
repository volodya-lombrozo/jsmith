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

import com.github.lombrozo.jsmith.guard.InMemoryCompiler;
import com.jcabi.log.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test cases for {@link RandomJavaClass}.
 * @since 0.1
 */
final class RandomJavaClassTest {

    @Test
    void retrievesTheSourceCode() {
        MatcherAssert.assertThat(
            "We expect that random class will generate at least something",
            new RandomJavaClass().src(),
            Matchers.not(Matchers.emptyString())
        );
    }

    @Test
    void generatesSameSrc() {
        final Params params = new Params();
        final RandomJavaClass clazz = new RandomJavaClass(params);
        MatcherAssert.assertThat(
            String.format(
                "We expect that the generated source code will be the same, but they weren't '%s'",
                params
            ),
            clazz.src(),
            Matchers.equalTo(clazz.src())
        );
    }

    @Test
    void generatesJavaCodeWithTheSameSeedAndParam() {
        final long seed = -4_887_843_732_314_896_880L;
        MatcherAssert.assertThat(
            "We expect that the generated source code will be the same for the same seed",
            new RandomJavaClass(new Params(seed)).src(),
            Matchers.equalTo(new RandomJavaClass(new Params(seed)).src())
        );
    }

    @Test
    void generatesSpecificJavaClass() {
        final String src = new RandomJavaClass(
            new Params(7_648_701_033_033_712_484L)
        ).src();
        Logger.info(this, "Generated source code: %n%s%n", src);
        Assertions.assertDoesNotThrow(
            () -> new InMemoryCompiler().compile(src),
            "The generated source code should be compilable"
        );
    }

    @ParameterizedTest
    @MethodSource("programs")
    void createsCompilableJavaSourceCode(final Params params, final String src) {
        Logger.info(this, "Params [%s]  Generated source code: %n%s%n", params, src);
        Assertions.assertDoesNotThrow(
            () -> new InMemoryCompiler().compile(src),
            String.format(
                "The generated source code should be compilable. You can reproduce the test using the following params: %s",
                params
            )
        );
    }

    /**
     * Generates many Java classes with different convergence factor value.
     * This is a performance test, so it is disabled by default.
     * If you want to run it, remove the {@link Disabled} annotation.
     * @todo #92:30min Convergence factor doesn't affect the generated source code.
     *  Actually if we change the convergence factor, the generated source code size
     *  should be different. We need to find the reason why it doesn't work and fix it.
     */
    @Test
    @Disabled
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void generatesRandomJavaClassWithChangedConvergence() {
        final List<String> results = new ArrayList<>(10);
        for (double factor = 0.1; factor < 1.0; factor += 0.1) {
            Logger.info(this, "Generating program for factor: %.1f", factor);
            final long start = System.currentTimeMillis();
            final double current = factor;
            final int attempts = 100;
            final int length = IntStream.range(0, attempts)
                .mapToObj(i -> new RandomJavaClass(new Params(current)))
                .map(RandomJavaClass::src)
                .mapToInt(String::length)
                .sum();
            final long end = System.currentTimeMillis();
            results.add(
                String.format(
                    "Factor: %.1f, total length: %d, attempts: %d time: %d ms",
                    current,
                    length,
                    attempts,
                    end - start
                )
            );
        }
        Logger.info(this, "Results: %n%s%n", String.join("\n", results));
    }

    /**
     * Generate random programs.
     * @return Stream of random programs.
     */
    static Stream<Arguments> programs() {
        return Stream.generate(Params::new)
            .limit(10)
            .map(params -> Arguments.of(params, new RandomJavaClass(params).src()));
    }
}
