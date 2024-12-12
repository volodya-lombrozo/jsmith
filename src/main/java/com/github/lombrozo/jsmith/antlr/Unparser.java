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
package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import java.util.HashMap;
import java.util.Map;

/**
 * Unparser that contains all parser rules.
 * It generates a string representation of the parser rule.
 * @since 0.1
 */
public final class Unparser {

    /**
     * All the parser rules.
     */
    private final Map<String, Rule> rules;

    /**
     * Default constructor.
     */
    public Unparser() {
        this(new HashMap<>(0));
    }

    /**
     * Constructor.
     * @param all All the parser rules.
     */
    private Unparser(final Map<String, Rule> all) {
        this.rules = all;
    }

    /**
     * Add a parser rule.
     * @param name Rule name.
     * @param rule Parser rule.
     * @return This unparser.
     */
    public Unparser with(final String name, final Rule rule) {
        this.rules.put(name, rule);
        return this;
    }

    /**
     * Generate a string representation of the parser rule.
     * @param rule Rule.
     * @param context Context.
     * @return String representation of the parser rule.
     */
    public Snippet generate(final String rule, final Context context) {
        return this.rule(rule).generate(context);
    }

    /**
     * Get a parser rule by name.
     * @param name Rule name.
     * @return Parser rule.
     */
    public Rule rule(final String name) {
        if (!this.rules.containsKey(name)) {
            throw new IllegalStateException(
                String.format("Rule not found: %s. All available rules: [%s]", name, this.rules)
            );
        }
        return this.rules.get(name);
    }

}
