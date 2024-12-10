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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Text output labels.
 * You can label text to format it in a specific way.
 * @since 0.1
 */
public final class Labels {

    /**
     * Is the text output produced by a rule.
     */
    private final boolean rule;

    /**
     * Additional custom attributes.
     */
    private final Map<String, String> properties;

    public Labels() {
        this(false);
    }

    /**
     * Default constructor.
     * @param rule Is the text output produced by a rule.
     */
    public Labels(final boolean rule) {
        this(rule, new HashMap<>(0));
    }

    /**
     * Constructor.
     * @param rule Is the text output produced by a rule.
     * @param additional Additional attributes.
     */
    Labels(
        final boolean rule,
        final Map<String, String> additional
    ) {
        this.rule = rule;
        this.properties = additional;
    }

    /**
     * Additional custom attributes.
     * @return Additional attributes.
     */
    public Map<String, String> custom() {
        return Collections.unmodifiableMap(this.properties);
    }

    /**
     * Check if contains a label.
     * @param key Label key.
     * @return True if contains.
     */
    public boolean contains(final String key) {
        return this.properties.containsKey(key);
    }

    public Labels with(final String key, final String value) {
        final Map<String, String> map = new HashMap<>(this.properties);
        map.put(key, value);
        return new Labels(this.rule, map);
    }

    /**
     * Remove an attribute.
     * @param key Key.
     */
    public void without(final String key) {
        this.properties.remove(key);
    }

    /**
     * Add an attribute.
     * @param other Other attributes.
     * @return New attributes.
     */
    public Labels add(final Labels other) {
        final Map<String, String> map = new HashMap<>(this.properties);
        map.putAll(other.custom());
        return new Labels(this.rule || other.isRule(), map);
    }

    /**
     * Is the text output produced by a rule.
     * @return True if the text output produced by a rule.
     */
    boolean isRule() {
        return this.rule;
    }
}
