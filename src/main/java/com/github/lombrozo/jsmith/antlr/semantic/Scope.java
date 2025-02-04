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

import com.github.lombrozo.jsmith.random.Rand;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.ToString;

/**
 * Scope.
 * @since 0.1
 */
@ToString
@SuppressWarnings("PMD.TooManyMethods")
public final class Scope {

    /**
     * Parent scope.
     */
    @ToString.Exclude
    private final Scope parent;

    /**
     * All scope variables.
     */
    private final Variables variables;

    /**
     * Random generator.
     */
    @ToString.Exclude
    private final Rand rand;

    /**
     * Constructor.
     * @param rand Random generator.
     */
    public Scope(final Rand rand) {
        this(null, new Variables(), rand);
    }

    /**
     * Constructor.
     * @param parent Parent scope.
     * @param rand Random generator.
     */
    public Scope(final Scope parent, final Rand rand) {
        this(parent, new Variables(), rand);
    }

    /**
     * Constructor.
     * @param parent Parent scope.
     * @param variables Variables in the scope.
     * @param rand Random generator.
     */
    private Scope(
        final Scope parent,
        final Variables variables,
        final Rand rand
    ) {
        this.parent = parent;
        this.variables = variables;
        this.rand = rand;
    }

    @ToString.Include
    public String identifier() {
        return String.valueOf(System.identityHashCode(this));
    }

    /**
     * Declare a variable.
     * @param name Variable name.
     */
    void declare(final String name) {
        this.variables.declare(name);
    }

    /**
     * Declare a variable.
     * @param name Variable name.
     * @param type Variable type.
     */
    void declare(final String name, final String type) {
        this.variables.declare(name, type);
    }

    /**
     * Assign all declared variables.
     * @param name Variable name.
     */
    void init(final String name) {
        this.variables.assign(name);
    }

    /**
     * Get a random declared variable.
     * @return Random declared variable.
     */
    Optional<String> declared() {
        return this.random(this.allDeclared());
    }

    /**
     * Get a type of the variable.
     * @param name Variable name.
     * @return Variable type.
     */
    String type(final String name) {
        return this.variables.type(name);
    }

    /**
     * Get a random initialized variable.
     * @return Random initialized variable.
     */
    Optional<String> initialized() {
        return this.random(this.allAssigned());
    }

    /**
     * Get a random initialized variable of the given type.
     * @param type Variable type.
     * @return Random initialized variable.
     */
    Optional<String> initialized(final String type) {
        return this.random(this.variables.allAssigned(type));
    }

    /**
     * Get all declared variables.
     * @return All declared variables.
     */
    private Set<String> allDeclared() {
        return Stream.concat(
            this.variables.allDeclared().stream(),
            Optional.ofNullable(this.parent)
                .map(Scope::allDeclared)
                .stream()
                .flatMap(Collection::stream)
        ).collect(Collectors.toSet());
    }

    /**
     * Get all assigned variables.
     * @return All assigned variables.
     */
    private Set<String> allAssigned() {
        return Stream.concat(
            this.variables.allAssigned().stream(),
            Optional.ofNullable(this.parent)
                .map(Scope::allAssigned)
                .stream()
                .flatMap(Collection::stream)
        ).collect(Collectors.toSet());
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
