/*
 * The MIT License
 *
 * Copyright (c) 2023 Volodya Lombrozo
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.lombrozo.jsmith;

/**
 * Random Java class.
 * @since 0.1
 */
public final class RandomJavaClass {

    /**
     * Name of the class.
     */
    private final String name;

    /**
     * Source code of the class.
     */
    private final String source;

    /**
     * Constructor.
     */
    public RandomJavaClass() {
        this("HelloWorld", RandomJavaClass.helloWorld());
    }

    /**
     * Constructor.
     * @param name Name of the class.
     * @param source Source code of the class.
     */
    public RandomJavaClass(final String name, final String source) {
        this.name = name;
        this.source = source;
    }

    /**
     * Name of the class.
     * @return Name of the class.
     */
    public String name() {
        return this.name;
    }

    /**
     * Source code of the class.
     * @return Source code of the class.
     */
    public String src() {
        return this.source;
    }

    /**
     * Hello world source code.
     * @return Source code of "hello world" java program.
     */
    private static String helloWorld() {
        return String.join(
            "\n",
            "public class HelloWorld {",
            "    public static void main(String[] args) {",
            "        System.out.println(\"Hello, World!\");",
            "    }",
            "}"
        );
    }
}
