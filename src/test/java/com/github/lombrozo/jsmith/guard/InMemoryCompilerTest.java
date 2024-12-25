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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link InMemoryCompiler}.
 * @since 0.2
 */
final class InMemoryCompilerTest {

    @Test
    void compilesHelloWorld() throws Exception {
        MatcherAssert.assertThat(
            "Hello world is expected",
            (String) new InMemoryCompiler()
                .compile(
                    "public class HelloWorld { public static String hello() {return \"Hello world\";}}"
                ).getDeclaredMethod("hello")
                .invoke(null),
            Matchers.equalTo("Hello world")
        );
    }

    @Test
    void compilesHelloWorldWithPackage() throws Exception {
        MatcherAssert.assertThat(
            "Hello world with package is expected",
            (String) new InMemoryCompiler()
                .compile(
                    "package com.github.lombrozo;\n public class HelloWorld { public static String hello() {return \"Hello world with package\";}}"
                ).getDeclaredMethod("hello")
                .invoke(null),
            Matchers.equalTo("Hello world with package")
        );
    }
}
