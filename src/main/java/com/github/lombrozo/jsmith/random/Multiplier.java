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

import com.github.lombrozo.jsmith.antlr.rules.Empty;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.Collections;

/**
 * Multiplier of rules.
 * For example, the rule {@code a+} can be represented as
 * {@code new OneOrMore(new Rand(), new LexerAtom("a"))}.
 * The idea is to generate a random number of repetitions of the rule based on EBNF suffix
 * like {@code +}, {@code *}, {@code ?}.
 * @since 0.1
 */
@FunctionalInterface
public interface Multiplier {

    /**
     * Multiply element based on the implementation.
     *
     * @param element Element to repeat.
     * @return Generated string by the repetition.
     */
    Rule repeat(final Rule element);

    /**
     * Exactly one repetition.
     * @since 0.1
     */
    final class One implements Multiplier {

        @Override
        public Rule repeat(final Rule element) {
            return element;
        }
    }

    /**
     * Zero or one repetition.
     * @since 0.1
     */
    final class ZeroOrOne implements Multiplier {

        /**
         * Random generator.
         */
        private final Rand rand;

        /**
         * Constructor.
         */
        public ZeroOrOne() {
            this(new Rand());
        }

        /**
         * Constructor.
         * @param rand Random generator.
         */
        public ZeroOrOne(final Rand rand) {
            this.rand = rand;
        }

        @Override
        public Rule repeat(final Rule element) {
            final Rule res;
            if (this.rand.flip()) {
                res = element;
            } else {
                res = new Empty();
            }
            return res;
        }
    }

    /**
     * One or more repetitions.
     * @since 0.1
     */
    final class OneOrMore implements Multiplier {

        /**
         * Random generator.
         */
        private final Rand rand;

        /**
         * Limit of repetitions.
         */
        private final int limit;

        /**
         * Constructor.
         */
        public OneOrMore() {
            this(new Rand());
        }

        /**
         * Constructor.
         * @param rand Random generator.
         */
        public OneOrMore(final Rand rand) {
            this(rand, 5);
        }

        /**
         * Constructor.
         * @param rand Random generator.
         * @param limit Limit of repetitions.
         */
        public OneOrMore(final Rand rand, final int limit) {
            this.rand = rand;
            this.limit = limit;
        }

        @Override
        public Rule repeat(final Rule element) {
            return new Several(Collections.nCopies(this.rand.range(this.limit) + 1, element));
        }
    }

    /**
     * Zero or more repetitions.
     * @since 0.1
     */
    final class ZeroOrMore implements Multiplier {

        /**
         * Random generator.
         */
        private final Rand rand;

        /**
         * Limit of repetitions.
         */
        private final int limit;

        /**
         * Constructor.
         */
        public ZeroOrMore() {
            this(new Rand());
        }

        /**
         * Constructor.
         * @param rand Random generator.
         */
        public ZeroOrMore(final Rand rand) {
            this(rand, 5);
        }

        /**
         * Constructor.
         * @param rand Random generator.
         * @param limit Limit of repetitions.
         */
        public ZeroOrMore(final Rand rand, final int limit) {
            this.rand = rand;
            this.limit = limit;
        }

        @Override
        public Rule repeat(final Rule element) {
            return new Several(Collections.nCopies(this.rand.range(this.limit), element));
        }
    }
}
