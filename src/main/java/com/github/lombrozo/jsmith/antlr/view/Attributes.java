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
package com.github.lombrozo.jsmith.antlr.view;

import java.util.HashMap;
import java.util.Map;

/**
 * Text output attributes.
 * @since 0.1
 */
public final class Attributes {

    /**
     * Is the text output produced by a rule.
     */
    private final boolean rule;

    private final Map<String, String> additional;

    /**
     * Default constructor.
     * @param rule Is the text output produced by a rule.
     */
    Attributes(final boolean rule) {
        this(rule, new HashMap<>(0));
    }

    Attributes(
        final boolean rule,
        final Map<String, String> additional
    ) {
        this.rule = rule;
        this.additional = additional;
    }

    /**
     * Is the text output produced by a rule.
     * @return True if the text output produced by a rule.
     */
    boolean isRule() {
        return this.rule;
    }

    public Map<String, String> additional() {
        return this.additional;
    }

    public Attributes add(final Attributes other) {
        final Map<String, String> map = new HashMap<>(this.additional);
        map.putAll(other.additional());
        return new Attributes(this.rule || other.isRule(), map);
    }
}
