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
package com.github.lombrozo.jsmith.antlr.semantic;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Variables.
 * @since 0.1
 * @todo #92:30min Move Variable to Context.
 *  Currently we use this class in the context of the generation tree building.
 *  We should move this class to the Context class, as it is used only during generation.
 *  After moving the class, we should update all the references to it.
 *  Moreover, we have strange behavior with cache which is rather confusing.
 */
public final class Variables {

    private final Set<String> names;
    private final Set<String> cache;

    public Variables() {
        this(new HashSet<>(0));
    }

    public Variables(Set<String> names) {
        this.names = names;
        this.cache = new HashSet<>(0);
    }

    public void declare(final String name) {
        this.cache.add(name);
    }

    public void closeStatement() {
        this.names.addAll(this.cache);
        this.cache.clear();
    }

    Optional<String> retrieve() {
        return this.names.stream().findAny();
    }
}