/*
 * MIT License
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.lombrozo.jsmith;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link RandomJavaClass}.
 * @since 0.1
 */
class RandomJavaClassTest {

    @Test
    void retrievesTheName() {
        MatcherAssert.assertThat(
            "Name of the class is not equal to the expected one",
            new RandomJavaClass().name(),
            Matchers.equalTo("HelloWorld")
        );
    }

    @Test
    void retrievesTheSourceCode() {
        MatcherAssert.assertThat(
            "Source code of the class is not equal to the expected 'hello-world' program",
            new RandomJavaClass().src(),
            Matchers.equalTo(
                String.join(
                    "\n",
                    "public class HelloWorld {",
                    "    public static void main(String[] args) {",
                    "        System.out.println(\"Hello, World!\");",
                    "    }",
                    "}"
                )
            )
        );
    }
}