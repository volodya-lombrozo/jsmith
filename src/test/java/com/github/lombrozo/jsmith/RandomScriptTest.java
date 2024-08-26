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
package com.github.lombrozo.jsmith;

import com.mifmif.common.regex.Generex;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link RandomScript}.
 */
final class RandomScriptTest {

    @Test
    void generatesSimpleGrammarSuccessfully() {
        final RandomScript script = new RandomScript(new ResourceOf("grammars/Simple.g4"));
        System.out.println(script.spec());
        for (int i = 0; i < 10; ++i) {
            System.out.println(script.generate("expr"));
        }
    }

    @Test
    void generatesArithmeticGrammarSuccessfully() {
        final RandomScript script = new RandomScript(new ResourceOf("grammars/Arithmetic.g4"));
        System.out.println(script.spec());
        for (int i = 0; i < 10; ++i) {
            System.out.println(script.generate("stat"));
        }
    }

    @Test
    void regexGenerationTest() {
        Generex generex = new Generex("[0-3]+");
        System.out.println(generex.random());
        Generex second = new Generex("[0-3]");
        System.out.println(second.random());
    }

    @Test
    void checksRandomizer() {
        final Rand rand = new Rand();
        for (int i = 0; i < 50; ++i) {
            System.out.println(rand.nextInt(10));
        }
    }

}