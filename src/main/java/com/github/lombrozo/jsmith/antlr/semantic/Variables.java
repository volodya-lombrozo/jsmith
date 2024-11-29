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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.ToString;

/**
 * Variables.
 * @since 0.1
 * @todo #92:30min Move Variable to Context.
 *  Currently we use this class in the context of the generation tree building.
 *  We should move this class to the Context class, as it is used only during generation.
 *  After moving the class, we should update all the references to it.
 *  Moreover, we have strange behavior with cache which is rather confusing.
 */
@ToString
public final class Variables {

    /**
     * Declared variables.
     */
    private final Set<String> decl;

    /**
     * Assigned variables.
     */
    private final Set<String> init;

    /**
     * Random generator.
     */
    @ToString.Exclude
    private final Rand rand;

    /**
     * Default constructor.
     * Initializes empty lists.
     */
    public Variables() {
        this(
            new HashSet<>(0),
            new HashSet<>(0),
            new Rand()
        );
    }

    /**
     * Constructor.
     * @param assigned Assigned variables.
     * @param declared Declared variables.
     * @param rand Random generator.
     */
    public Variables(
        final Set<String> assigned,
        final Set<String> declared,
        final Rand rand
    ) {
        this.init = assigned;
        this.decl = declared;
        this.rand = rand;
    }

    /**
     * Declare a variable.
     * @param name Variable name.
     */
    void declare(final String name) {
        this.decl.add(name);
    }

    /**
     * Assign all declared variables.
     * @param name Variable name.
     */
    void assign(final String name) {
        this.init.add(name);
    }

    /**
     * Get a random declared variable.
     * @return Random declared variable.
     */
    Optional<String> declared() {
        return this.random(this.decl);
    }

    /**
     * Get a random initialized variable.
     * @return Random initialized variable.
     */
    Optional<String> initialized() {
        return this.random(this.init);
    }

    /**
     * Get all declared variables.
     * @return All declared variables.
     */
    List<String> allDeclared() {
        return List.copyOf(this.decl);
    }

    /**
     * Get all assigned variables.
     * @return All assigned variables.
     */
    List<String> allAssigned() {
        return List.copyOf(this.init);
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
