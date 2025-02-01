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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.ToString;

/**
 * Variables.
 * @since 0.1
 */
@ToString
public final class Variables {

    /**
     * Declared variables.
     */
    private final Set<Variable> decl;

    /**
     * Assigned variables.
     */
    private final Set<Variable> init;

    /**
     * Default constructor.
     * Initializes empty lists.
     */
    public Variables() {
        this(
            new HashSet<>(0),
            new HashSet<>(0)
        );
    }

    /**
     * Constructor.
     * @param assigned Assigned variables.
     * @param declared Declared variables.
     */
    public Variables(
        final Set<Variable> assigned,
        final Set<Variable> declared
    ) {
        this.init = assigned;
        this.decl = declared;
    }

    /**
     * Get a type of the variable.
     * @param name Variable name.
     * @return Variable type.
     */
    public String type(final String name) {
        return Stream.concat(this.init.stream(), this.decl.stream())
            .filter(v -> v.name().equals(name)).findFirst().map(Variable::type).orElse("");
    }

    /**
     * Declare a variable.
     * @param name Variable name.
     */
    void declare(final String name) {
        this.decl.add(new Variable(name));
    }

    /**
     * Declare a variable.
     * @param name Variable name.
     * @param type Variable type.
     */
    void declare(final String name, final String type) {
        this.decl.add(new Variable(name, type));
    }

    /**
     * Assign all declared variables.
     * @param name Variable name.
     */
    void assign(final String name) {
        final Variable declared = this.decl.stream().filter(v -> v.name().equals(name)).findFirst()
            .orElseThrow(() -> new IllegalStateException("Variable is not declared"));
        this.init.add(declared);
    }

    /**
     * Get all declared variables.
     * @return All declared variables.
     */
    List<String> allDeclared() {
        return this.decl.stream().map(Variable::name).collect(Collectors.toList());
    }

    /**
     * Get all assigned variables.
     * @return All assigned variables.
     */
    List<String> allAssigned() {
        return this.init.stream().map(Variable::name).collect(Collectors.toList());
    }

    /**
     * Get all assigned variables of the given type.
     * @param type Variable type.
     * @return All assigned variables.
     */
    Set<String> allAssigned(final String type) {
        return this.init.stream()
            .filter(v -> v.type().equals(type))
            .map(Variable::name)
            .collect(Collectors.toSet());
    }
}
