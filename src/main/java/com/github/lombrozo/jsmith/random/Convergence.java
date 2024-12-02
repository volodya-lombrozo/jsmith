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

import com.github.lombrozo.jsmith.Params;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class allows randomly choosing one element among others by relative weights.
 * This class is needed to ensure convergence of the program generation algorithm.
 * Should be used in {@link Rand}.
 * @param <T> Type of the elements.
 * @since 0.1
 */
final class Convergence<T> {

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
    Convergence() {
        this(0.5d, false);
    }

    /**
     * Constructor.
     * @param params Generation params.
     */
    Convergence(final Params params) {
        this(params.factor(), 1.0d, new Rand(params.seed()), false);
    }

    /**
     * Constructor.
     * @param factor Factor of convergence.
     * @param verbose Do we need to log changes in the weights?
     */
    Convergence(final double factor, final boolean verbose) {
        this(factor, 1.0d, new Rand(), verbose);
    }

    /**
     * Constructor.
     * @param factor Factor of convergence.
     * @param weight Initial weight of the elements.
     * @param rand Random generator.
     * @param verbose Verbose mode.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    private Convergence(
        final double factor,
        final double weight,
        final Rand rand,
        final boolean verbose
    ) {
        this(factor, weight, new HashMap<>(0), rand, verbose);
    }

    /**
     * Constructor.
     * @param factor Factor of convergence.
     * @param weight Initial weight of the elements.
     * @param weights Weights of the elements.
     * @param rand Random generator.
     * @param verbose Verbose mode.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    private Convergence(
        final double factor,
        final double weight,
        final Map<T, Map<T, Double>> weights,
        final Rand rand,
        final boolean verbose
    ) {
        this.factor = factor;
        this.weight = weight;
        this.weights = weights;
        this.rand = rand;
        this.verbose = verbose;
    }

    /**
     * Copy this object.
     * @return Copy of this object.
     */
    public Convergence<T> copy() {
        return new Convergence<>(
            this.factor,
            this.weight,
            this.weightsCopy(),
            this.rand,
            this.verbose
        );
    }

    /**
     * Choose one of the child elements from a parent element.
     * @param from The main parent element which has child elements.
     * @param elements Child elements.
     * @return Chosen element.
     */
    @SuppressWarnings("unchecked")
    T choose(final T from, final Collection<T> elements) {
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
    T choose(final T from, final T... elements) {
        if (this.factor < 0 || this.factor > 1) {
            throw new IllegalArgumentException("Factor must be between 0 and 1");
        }
        if (elements.length == 0) {
            throw new IllegalArgumentException(
                String.format("No elements to choose from for '%s' element", from)
            );
        }
        final Map<T, Double> current = this.weights.computeIfAbsent(
            from, key -> this.init(elements)
        );
        this.info(String.format("Weights for '%s': '%s'", from, current));
        final double[] cumulative = new double[current.size()];
        final Object[] all = new Object[current.size()];
        double total = 0d;
        int index = 0;
        for (final Map.Entry<T, Double> entry : current.entrySet()) {
            total = total + entry.getValue();
            all[index] = entry.getKey();
            cumulative[index] = total;
            ++index;
        }
        final double random = this.rand.floating() * total;
        final int length = cumulative.length;
        for (int point = 0; point < length; ++point) {
            if (cumulative[point] >= random) {
                final T element = (T) all[point];
                this.info(
                    String.format("Chosen '%s' with weight '%s'", element, current.get(element))
                );
                current.put(element, current.get(element) * this.factor);
                return element;
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
        final Map<T, Double> res = new LinkedHashMap<>(0);
        for (final T element : elements) {
            res.putIfAbsent(element, 0d);
            res.computeIfPresent(element, (key, value) -> value + this.weight);
        }
        return res;
    }

    /**
     * Copy weights deeply.
     * @return Copy of the weights deeply.
     */
    private Map<T, Map<T, Double>> weightsCopy() {
        final Map<T, Map<T, Double>> copy = new LinkedHashMap<>(0);
        for (final Map.Entry<T, Map<T, Double>> entry : this.weights.entrySet()) {
            copy.put(entry.getKey(), new LinkedHashMap<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Log a message if verbose mode is enabled.
     * @param msg Message to print.
     */
    private void info(final String msg) {
        if (this.verbose) {
            Convergence.LOG.info(msg);
        }
    }
}
