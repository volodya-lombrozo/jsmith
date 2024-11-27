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

import com.github.lombrozo.jsmith.random.Rand;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scope.
 * @since 0.1
 */
public final class Scope {

    private final Scope parent;

    /**
     * All scope variables.
     */
    private final Variables variables;
    /**
     * Inner scopes.
     */
    private final List<Scope> inner;

    /**
     * Random generator.
     */
    private final Rand rand;

    /**
     * Constructor.
     */
    public Scope() {
        this(new Variables(), new ArrayList<>(0));
    }

    public Scope(final Scope parent) {
        this(parent, new Variables(), new ArrayList<>(0), new Rand());
    }

    /**
     * Constructor.
     * @param variables Variables.
     * @param inner Inner scopes.
     */
    public Scope(final Variables variables, final List<Scope> inner) {
        this(variables, inner, new Rand());
    }

    /**
     * Constructor.
     * @param variables Variables.
     * @param inner Inner scopes.
     * @param rand Random generator.
     * @todo: remove null
     */
    public Scope(final Variables variables, final List<Scope> inner, final Rand rand) {
        this(null, variables, inner, rand);
    }

    public Scope(
        final Scope parent,
        final Variables variables,
        final List<Scope> inner,
        final Rand rand
    ) {
        this.parent = parent;
        this.variables = variables;
        this.inner = inner;
        this.rand = rand;
    }

    /**
     * Declare a variable.
     * @param name Variable name.
     */
    void declare(final String name) {
        this.variables.declare(name);
    }

    /**
     * Assign all declared variables.
     * @param name Variable name.
     */
    void assign(final String name) {
        this.variables.assign(name);
    }

    void append(final Scope scope) {
        this.inner.add(scope);
    }

    /**
     * Get a random declared variable.
     * @return Random declared variable.
     */
    Optional<String> declared() {
        return this.random(this.allDeclared());
    }

    /**
     * Get a random initialized variable.
     * @return Random initialized variable.
     */
    Optional<String> initialized() {
        return this.random(this.allAssigned());
    }

    public Set<String> allIdentifiers() {
        return Stream.concat(
            this.allAssigned().stream(),
            this.allDeclared().stream()
        ).collect(Collectors.toSet());
    }

    /**
     * Get all declared variables.
     * @return All declared variables.
     */
    private Set<String> allDeclared() {
        final Set<String> res = Stream.concat(
            this.variables.allDeclared().stream(),
            Optional.ofNullable(this.parent)
                .map(Scope::allDeclared)
                .stream()
                .flatMap(Collection::stream)
        ).collect(Collectors.toSet());
        return res;
    }

    /**
     * Get all assigned variables.
     * @return All assigned variables.
     */
    private Set<String> allAssigned() {
        final Set<String> collect = Stream.concat(
            this.variables.allAssigned().stream(),
            Optional.ofNullable(this.parent)
                .map(Scope::allAssigned)
                .stream()
                .flatMap(Collection::stream)
        ).collect(Collectors.toSet());
        return collect;
    }

    /**
     * Get a random element from the collection.
     * @param collection Collection.
     * @return Random element.
     */
    private Optional<String> random(final Set<String> collection) {
        final Optional<String> result;
        if (collection.isEmpty()) {
            result = Optional.empty();
        } else {
            result = collection.stream().skip(this.rand.range(collection.size())).findFirst();
        }
        return result;
    }
}
