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
     * Additional custom attributes.
     */
    private final Map<String, String> properties;

    /**
     * Default constructor.
     */
    public Labels() {
        this(new HashMap<>(0));
    }

    public Labels(final String key, final String value) {
        this(new HashMap<>(Collections.singletonMap(key, value)));
    }

    /**
     * Constructor.
     * @param additional Additional attributes.
     */
    Labels(final Map<String, String> additional) {
        this.properties = additional;
    }

    /**
     * Check if contains a label.
     * @param key Label key.
     * @return True if contains.
     */
    public boolean contains(final String key) {
        return this.properties.containsKey(key);
    }
}
