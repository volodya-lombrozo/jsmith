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
package com.github.lombrozo.jsmith.antlr.semantic;

/**
 * Variable abstraction.
 * @since 0.1
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
final class Variable {

    /**
     * Variable name.
     */
    private final String name;

    /**
     * Variable type.
     */
    private final String type;

    /**
     * Constructor.
     * @param name Variable name.
     */
    Variable(final String name) {
        this(name, "");
    }

    /**
     * Constructor.
     * @param name Variable name.
     * @param type Variable type.
     */
    Variable(final String name, final String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Variable name.
     * @return Name.
     */
    public String name() {
        return this.name;
    }

    /**
     * Variable type.
     * @return Type.
     */
    String type() {
        return this.type;
    }
}
