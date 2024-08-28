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

import com.github.lombrozo.jsmith.antlr.rules.RuleDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class allows randomly choosing one element among others by relative weights.
 * This class is needed to ensure convergence of the program generation algorithm.
 * Should be used in {@link Rand}.
 * @since 0.1
 */
public final class Сonvergence {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Сonvergence.class.getName());

    /**
     * Factor of convergence.
     * This is a multiplier for the weights.
     * Each time when one of the elements is chosen, its weight is multiplied by this factor.
     * Must be between 0 and 1.
     */
    private final double factor;

    /**
     * Weights of the elements.
     */
    private final Map<String, Map<String, Double>> weights;

    public Сonvergence(final double factor) {
        if (factor < 0 || factor > 1) {
            throw new IllegalArgumentException("Factor must be between 0 and 1");
        }
        this.factor = factor;
        this.weights = new HashMap<>(0);
    }

    public String choose(final RuleDefinition main, final RuleDefinition... productions) {
        final String name = main.toString();
        final Map<String, Double> current = this.weights.computeIfAbsent(
            name, key -> this.init(productions)
        );
        Сonvergence.LOG.info(() -> String.format("Weights for '%s': '%s'", name, current));


        final double totalWeight = current.values().stream().mapToDouble(Double::doubleValue).sum();
        final double random = Math.random() * totalWeight;

        double sum = 0;
        for (Map.Entry<String, Double> entry : current.entrySet()) {
            sum += entry.getValue();
            if (sum >= random) {
                current.put(entry.getKey(), entry.getValue() * this.factor);
                return entry.getKey();
            }
        }
        throw new IllegalStateException("No production was chosen");
    }

    private Map<String, Double> init(
        final RuleDefinition... productions
    ) {
        final Map<String, Double> res = new HashMap<>(0);
        double total = 0d;
        for (final RuleDefinition production : productions) {
            final double initial = 1.0;
            total += initial;
            res.put(production.toString(), total);
        }
        return res;
    }


}
