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

final class InMemoryCompiler {


    private final JavaCompiler compiler;

    InMemoryCompiler() {
        this(ToolProvider.getSystemJavaCompiler());
    }

    private InMemoryCompiler(final JavaCompiler compiler) {
        this.compiler = compiler;
    }

    Class<?> compile(final String name, final String src) {
        return this.compile(new CompilationUnit(name, src)).get(0);
    }

    List<Class<?>> compile(final Path... files) throws IOException {
        CompilationUnit[] units = new CompilationUnit[files.length];
        for (int i = 0; i < files.length; i++) {
            final Path file = files[i];
            final String name = file.getFileName().toString().replace(".java", "");
            final String src = Files.readString(file);
            units[i] = new CompilationUnit(name, src);
        }
        return this.compile(units);
    }

    List<Class<?>> compile(final CompilationUnit... units) {
        try {
            return tryCompile(units);
        } catch (final MalformedURLException exception) {
            throw new IllegalStateException(
                String.format("Malformed URL for compilation units %s", Arrays.asList(units)),
                exception
            );
        } catch (final ClassNotFoundException exception) {
            throw new IllegalStateException(
                String.format(
                    "Some class with one of the names '%s' not found",
                    Arrays.stream(units).map(CompilationUnit::name).toArray()
                ),
                exception
            );
        }
    }

    private List<Class<?>> tryCompile(
        CompilationUnit... units
    ) throws MalformedURLException, ClassNotFoundException {
        //handle success
        final Boolean success = this.compiler.getTask(
            null,
            null,
            null,
            null,
            null,
            Arrays.stream(units).map(CompilationUnit::toJava).collect(Collectors.toList())
        ).call();
        final URLClassLoader loader = URLClassLoader.newInstance(
            new URL[]{new File("").toURI().toURL()}
        );
        List<Class<?>> res = new ArrayList<>(0);
        for (final CompilationUnit unit : units) {
            final Class<?> claszz = Class.forName(unit.name(), true, loader);
            res.add(claszz);
        }
        return res;
    }

    @ToString
    private static class CompilationUnit {

        private final String name;
        private final String src;

        public CompilationUnit(final String name, final String src) {
            this.name = name;
            this.src = src;
        }

        String name() {
            return name;
        }

        String src() {
            return src;
        }

        JavaFileObject toJava() {
            return new JavaSourceFromString(this.name, this.src);
        }

    }


    private static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
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
