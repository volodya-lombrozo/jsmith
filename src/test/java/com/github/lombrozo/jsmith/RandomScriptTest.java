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

import java.util.logging.Logger;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.RepeatedTest;

/**
 * Tests for {@link RandomScript}.
 * @since 0.1
 * @todo #1:90min Control Recursive RandomScript generation.
 *  Currently, if we run one of this tests many times we can encounter StackOverflowError.
 *  We need to control the recursive generation of RandomScript to avoid this error.
 *  Most probably some researches mentioned this problem and we can use their solutions.
 */
final class RandomScriptTest {

    /**
     * Logger.
     */
    private final Logger logger = Logger.getLogger("RandomScriptTest");

    @RepeatedTest(10)
    void generatesSimpleGrammarSuccessfully() {
        final RandomScript script = new RandomScript(new ResourceOf("grammars/Simple.g4"));
        this.logger.info(String.format("Simple spec (lisp format): %s", script.spec()));
        final String example = script.generate("expr");
        this.logger.info(String.format("Generated simple example:%n%s%n", example));
        MatcherAssert.assertThat(
            "We expect that the example for Simple grammar will be generated successfully",
            example,
            Matchers.not(Matchers.emptyString())
        );
    }

    @RepeatedTest(10)
    void generatesArithmeticGrammarSuccessfully() {
        final RandomScript script = new RandomScript(new ResourceOf("grammars/Arithmetic.g4"));
        this.logger.info(String.format("Arithmetic spec (lisp format): %s", script.spec()));
        final String example = script.generate("stat");
        this.logger.info(String.format("Generated Arithmetic example:%n%s%n", example));
        MatcherAssert.assertThat(
            "We expect that the example for Arithmetic grammar will be generated successfully",
            example,
            Matchers.not(Matchers.emptyString())
        );
    }
}