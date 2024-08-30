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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class allows randomly choosing one element among others by relative weights.
 * This class is needed to ensure convergence of the program generation algorithm.
 * Should be used in {@link Rand}.
 * @since 0.1
 */
public final class Convergence<T> {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Convergence.class.getName());

    /**
     * Factor of convergence.
     * This is a multiplier for the weights.
     * Each time when one of the elements is chosen, its weight is multiplied by this factor.
     * Must be between 0 and 1.
     */
    private final double factor;

    /**
     * Initial weight of the elements.
     * Usually, it is 1.
     */
    private final double weight;

    /**
     * Weights of the elements.
     */
    private final Map<T, Map<T, Double>> weights;

    /**
     * Random generator.
     */
    private final Rand rand;

    /**
     * Verbose mode.
     */
    private final boolean verbose;

    /**
     * Default constructor.
     */
    public Convergence() {
        this(0.5d);
    }

    /**
     * Constructor.
     * @param factor Factor of convergence.
     */
    public Convergence(final double factor) {
        this(factor, false);
    }

    /**
     * Constructor.
     * @param factor Factor of convergence.
     * @param verbose Do we need to log changes in the weights?
     */
    public Convergence(final double factor, final boolean verbose) {
        this(factor, 1.0d, new Rand(), verbose);
    }

    /**
     * Constructor.
     * @param factor Factor of convergence.
     * @param weight Initial weight of the elements.
     * @param rand Random generator.
     * @param verbose Verbose mode.
     */
    public Convergence(
        final double factor,
        final double weight,
        final Rand rand,
        final boolean verbose
    ) {
        this(factor, weight, new HashMap<>(0), rand, verbose);
    }

    public Convergence(
        final double factor,
        final double weight,
        final Map<T, Map<T, Double>> weights,
        final Rand rand,
        final boolean verbose
    ) {
        if (factor < 0 || factor > 1) {
            throw new IllegalArgumentException("Factor must be between 0 and 1");
        }
        this.factor = factor;
        this.weight = weight;
        this.weights = weights;
        this.rand = rand;
        this.verbose = verbose;
    }

    /**
     * Copy this object.
     */
    public Convergence<T> copy() {
        return new Convergence<>(
            this.factor,
            this.weight,
            this.wightsCopy(),
            this.rand,
            this.verbose
        );
    }

    private Map<T, Map<T, Double>> wightsCopy() {
        final Map<T, Map<T, Double>> copy = new HashMap<>(0);
        for (final Map.Entry<T, Map<T, Double>> entry : this.weights.entrySet()) {
            copy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Choose one of the child elements from a parent element.
     * @param from The main parent element which has child elements.
     * @param elements Child elements.
     * @return Chosen element.
     */
    @SuppressWarnings("unchecked")
    public T choose(final T from, final Collection<T> elements) {
        return this.choose(from, (T[]) elements.toArray(new Object[0]));
    }

    /**
     * Choose one of the child elements from a parent element.
     * Each time when we choose an element, its weight is multiplied by the factor.
     * So, the probability of choosing this element one more time decreases.
     * @param from The main parent element which has child elements.
     * @param elements Child elements.
     * @return Chosen element.
     */
    public T choose(final T from, final T... elements) {
        final Map<T, Double> current = this.weights.computeIfAbsent(
            from, key -> this.init(elements)
        );
        if (elements.length == 0) {
            throw new IllegalArgumentException(
                String.format("No elements to choose from for '%s' element", from)
            );
        }
        this.log(String.format("Weights for '%s': '%s'", from, current));
        final double total = current.values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElseThrow(() -> new IllegalStateException("No elements"));
        final double random = this.rand.floating() * total;
        double sum = 0;
        for (final Map.Entry<T, Double> entry : current.entrySet()) {
            sum += entry.getValue();
            if (sum >= random) {
                this.log(
                    String.format("Chosen '%s' with weight '%s'", entry.getKey(), entry.getValue())
                );
                current.put(entry.getKey(), entry.getValue() * this.factor);
                return entry.getKey();
            }
        }
        throw new IllegalStateException("No element was chosen");
    }

    /**
     * Init weights map for the elements.
     * @param elements Elements.
     * @return Initial map with weights.
     */
    private Map<T, Double> init(final T... elements) {
        final Map<T, Double> res = new HashMap<>(0);
        for (final T element : elements) {
            res.putIfAbsent(element, 0d);
            res.computeIfPresent(element, (key, value) -> value + this.weight);
        }
        return res;
    }

    /**
     * Log a message if verbose mode is enabled.
     * @param msg Message to print.
     */
    private void log(final String msg) {
        if (this.verbose) {
            Convergence.LOG.info(msg);
        }
    }
}
