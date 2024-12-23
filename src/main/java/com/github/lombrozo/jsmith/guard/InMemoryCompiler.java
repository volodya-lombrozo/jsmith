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
package com.github.lombrozo.jsmith.guard;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import lombok.ToString;

/**
 * In-memory compiler.
 * @since 0.2
 */
final class InMemoryCompiler {

    /**
     * Java compiler.
     */
    private final JavaCompiler compiler;

    /**
     * Default constructor.
     */
    InMemoryCompiler() {
        this(ToolProvider.getSystemJavaCompiler());
    }

    /**
     * Constructor.
     * @param compiler Java compiler
     */
    private InMemoryCompiler(final JavaCompiler compiler) {
        this.compiler = compiler;
    }

    /**
     * Compile source code.
     * @param name Name of the class.
     * @param src Source code.
     * @return Compiled class.
     */
    Class<?> compile(final String name, final String src) {
        return this.compile(new CompilationUnit(name, src)).get(0);
    }

    /**
     * Compile source code.
     * @param files Files with source code.
     * @return Compiled classes.
     * @throws IOException If an I/O error occurs
     */
    List<Class<?>> compile(final Path... files) throws IOException {
        final int length = files.length;
        final CompilationUnit[] units = new CompilationUnit[length];
        for (int index = 0; index < length; ++index) {
            final Path file = files[index];
            final String name = file.getFileName().toString().replace(".java", "");
            final String src = Files.readString(file);
            units[index] = new CompilationUnit(name, src);
        }
        return this.compile(units);
    }

    /**
     * Compile source code.
     * @param units Compilation units.
     * @return Compiled classes.
     */
    List<Class<?>> compile(final CompilationUnit... units) {
        try {
            return this.tryCompile(units);
        } catch (final MalformedURLException exception) {
            throw new IllegalStateException(
                String.format("Malformed URL for compilation units %s", Arrays.asList(units)),
                exception
            );
        } catch (final ClassNotFoundException exception) {
            throw new IllegalStateException(
                String.format(
                    "Some class with one of the names '%s' not found",
                    Arrays.asList(units)
                ),
                exception
            );
        } catch (final IOException exception) {
            throw new IllegalStateException(
                String.format("I/O error during compilation of units %s", Arrays.asList(units)),
                exception
            );
        }
    }

    /**
     * Try to compile source code.
     * @param units Compilation units to compile.
     * @return Compiled classes.
     * @throws MalformedURLException If a URL of a classloader is malformed.
     * @throws ClassNotFoundException If a class is not found.
     */
    private List<Class<?>> tryCompile(
        final CompilationUnit... units
    ) throws IOException, ClassNotFoundException, MalformedURLException {
        final boolean success = this.compiler.getTask(
            null,
            null,
            null,
            null,
            null,
            Arrays.stream(units).map(CompilationUnit::toJava).collect(Collectors.toList())
        ).call();
        if (!success) {
            throw new IllegalStateException(
                String.format("Compilation failed for units %s", Arrays.asList(units))
            );
        }
        final List<Class<?>> res = new ArrayList<>(0);
        try (URLClassLoader loader = InMemoryCompiler.loader()) {
            for (final CompilationUnit unit : units) {
                res.add(Class.forName(unit.name(), true, loader));
            }
        }
        return res;
    }

    /**
     * Create a class loader.
     * @return Class loader.
     * @throws MalformedURLException If a URL of a classloader is malformed.
     */
    private static URLClassLoader loader() throws MalformedURLException {
        return URLClassLoader.newInstance(
            new URL[]{new File("").toURI().toURL()}
        );
    }

    /**
     * Compilation unit.
     * @since 0.2
     */
    @ToString
    private static final class CompilationUnit {

        /**
         * Name of the class.
         */
        private final String clazz;

        /**
         * Source code.
         */
        @ToString.Exclude
        private final String src;

        /**
         * Constructor.
         * @param name Name of the class.
         * @param src Source code.
         */
        private CompilationUnit(final String name, final String src) {
            this.clazz = name;
            this.src = src;
        }

        /**
         * Name of the class.
         * @return Name of the class.
         */
        String name() {
            return this.clazz;
        }

        /**
         * Convert to a Java file object.
         * @return Java file object.
         */
        JavaFileObject toJava() {
            return new StringSource(this.clazz, this.src);
        }

    }

    /**
     * Java source code.
     * @since 0.2
     */
    private static class StringSource extends SimpleJavaFileObject {

        /**
         * Java source code.
         */
        private final String code;

        /**
         * Constructor.
         * @param name Name of the class.
         * @param code Java source code.
         */
        StringSource(final String name, final String code) {
            super(
                URI.create(
                    String.format(
                        "string:///%s%s",
                        name.replace('.', '/'),
                        Kind.SOURCE.extension
                    )
                ),
                Kind.SOURCE
            );
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(final boolean ignore) {
            return this.code;
        }
    }
}
