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
package com.github.lombrozo.jsmith.guard;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

/**
 * Custom Java file manager that stores compiled classes in memory.
 * @since 0.2
 */
public final class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    /**
     * The compiled classes.
     */
    private final List<JavaClass> compiled;

    /**
     * Constructor.
     * @param origin The original file manager
     */
    MemoryJavaFileManager(final JavaFileManager origin) {
        super(origin);
        this.compiled = new ArrayList<>(0);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
        final Location location,
        final String name,
        final JavaFileObject.Kind kind,
        final FileObject sibling
    ) {
        final JavaClass clazz = new JavaClass(name, kind);
        this.compiled.add(clazz);
        return clazz;
    }

    /**
     * Get the class loader.
     * @return The class loader.
     */
    ClassLoader loader() {
        return new MemoryClassLoader(this.compiled);
    }

    /**
     * A byte array class.
     * @since 0.1
     */
    private static final class JavaClass extends SimpleJavaFileObject {

        /**
         * The output stream.
         */
        private final ByteArrayOutputStream baos;

        /**
         * Simple name of the class.
         * Example: com.example.MyClass
         */
        private final String name;

        /**
         * Constructor.
         * @param name The name of the class.
         * @param kind The kind of the class.
         */
        private JavaClass(final String name, final Kind kind) {
            super(
                URI.create(
                    String.format("bytes:///%s%s", name.replace('.', '/'), kind.extension)
                ),
                kind
            );
            this.name = name;
            this.baos = new ByteArrayOutputStream();
        }

        @Override
        public OutputStream openOutputStream() {
            return this.baos;
        }

        /**
         * Get the full name.
         * @return The full name.
         */
        String fullName() {
            return this.name;
        }

        /**
         * Get the bytes.
         * @return The bytes.
         */
        byte[] bytes() {
            return this.baos.toByteArray();
        }
    }

    /**
     * A class loader that loads classes from memory.
     * @since 0.1
     */
    private static final class MemoryClassLoader extends ClassLoader {

        /**
         * All the classes.
         */
        private final Map<String, byte[]> classes;

        /**
         * Constructor.
         * @param classes The classes.
         */
        MemoryClassLoader(final List<? extends JavaClass> classes) {
            this(classes.stream().collect(Collectors.toMap(JavaClass::fullName, JavaClass::bytes)));
        }

        /**
         * Constructor.
         * @param all All the classes.
         */
        private MemoryClassLoader(final Map<String, byte[]> all) {
            this.classes = new HashMap<>(all);
        }

        @Override
        public Class<?> findClass(final String name) throws ClassNotFoundException {
            final byte[] bytes = this.classes.get(name);
            final Class<?> result;
            if (Objects.nonNull(bytes)) {
                result = this.defineClass(name, bytes, 0, bytes.length);
            } else {
                result = super.findClass(name);
            }
            return result;
        }
    }
}
