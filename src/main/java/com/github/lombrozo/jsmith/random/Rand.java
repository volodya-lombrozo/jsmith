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

import com.mifmif.common.regex.Generex;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Random generator.
 * @since 0.1
 */
public final class Rand {

    /**
     * Random.
     * We use {@link Random} inseatd of {@link SecureRandom} because we don't need a
     * cryptographically secure random. Additionally, we need to be able to seed the random
     * to reproduce the same results.
     */
    private final Random random;

    /**
     * Default constructor.
     */
    public Rand() {
        this(new Random());
    }

    /**
     * Constructor.
     * @param seed Random seed.
     */
    public Rand(final long seed) {
        this(new Random(seed));
    }

    /**
     * Constructor.
     * @param rand Java random.
     */
    private Rand(final Random rand) {
        this.random = rand;
    }

    /**
     * Generates a random integer.
     * @param bound Upper bound.
     * @return Random integer.
     */
    public int range(final int bound) {
        return this.random.nextInt(bound);
    }

    /**
     * Generates a random integer in a range.
     * @param min Minimum.
     * @param max Maximum.
     * @return Random integer.
     */
    public int range(final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                String.format("Min should be less than max, but min = %d, max = %d", min, max)
            );
        }
        return this.random.nextInt(max - min + 1) + min;
    }

    /**
     * Generates a random char in a range.
     * @param min Minimum char.
     * @param max Maximum char.
     * @return Random char.
     */
    public char range(final char min, final char max) {
        return this.range(max, min);
    }

    /**
     * Generates a random string for a given regex.
     * @param regex Regex for which to generate a string.
     * @return Random string.
     */
    public String regex(final String regex) {
        return new Generex(regex, this.random).random();
    }

    /**
     * Generates a random double.
     * @return Random double.
     */
    double floating() {
        return this.random.nextDouble();
    }

    /**
     * Flips a coin.
     * @return True if heads, false if tails.
     */
    boolean flip() {
        return this.random.nextBoolean();
    }

}
