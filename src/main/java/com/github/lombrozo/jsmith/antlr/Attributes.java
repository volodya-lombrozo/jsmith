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

import java.util.HashMap;
import java.util.Map;

public final class Attributes {
    private final Map<String, String> attributes;

    public Attributes() {
        this(new HashMap<>(0));
    }

    public Attributes(final Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Attributes put(final String key, final String value) {
        this.attributes.put(key, value);
        return this;
    }

    public String get(final String key) {
        return this.attributes.get(key);
    }

    /**
     * Add an attribute.
     * @param other Other attributes.
     * @return New attributes.
     */
    public Attributes add(final Attributes other) {
        final Map<String, String> map = new HashMap<>(this.attributes);
        map.putAll(other.attributes);
        return new Attributes(map);
    }

    public boolean contains(final String key) {
        return this.attributes.containsKey(key);
    }

    public Attributes with(final String comment, final String type) {
        this.attributes.put(comment, type);
        return this;
    }

    public void without(final String key) {
        this.attributes.remove(key);
    }
}
