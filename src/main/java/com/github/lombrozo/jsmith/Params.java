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
package com.github.lombrozo.jsmith;

import java.security.SecureRandom;
import lombok.ToString;

/**
 * Parameters for random generation.
 * @since 0.1
 */
@ToString
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class Params {

    /**
     * Default factor.
     */
    @ToString.Exclude
    private static final double DEFAULT_FACTOR = 0.5;

    /**
     * Default seed.
     */
    @ToString.Exclude
    private static final SecureRandom DEFAULT_SEED = new SecureRandom();

    /**
     * Factor.
     */
    private final double factor;

    /**
     * Seed.
     */
    private final long seed;

    /**
     * Constructor.
     */
    Params() {
        this(Params.DEFAULT_FACTOR);
    }

    /**
     * Constructor.
     * @param seed Seed.
     */
    Params(final long seed) {
        this(Params.DEFAULT_FACTOR, seed);
    }

    /**
     * Constructor.
     * @param factor Factor.
     */
    Params(final double factor) {
        this(factor, Params.DEFAULT_SEED.nextLong());
    }

    /**
     * Constructor.
     * @param factor Factor.
     * @param seed Seed.
     */
    private Params(final double factor, final long seed) {
        this.factor = factor;
        this.seed = seed;
    }

    /**
     * Factor.
     * @return Factor.
     */
    public double factor() {
        return this.factor;
    }

    /**
     * Seed.
     * @return Seed.
     */
    public long seed() {
        return this.seed;
    }
}
