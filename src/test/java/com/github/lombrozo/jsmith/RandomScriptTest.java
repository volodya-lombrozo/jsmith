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

import com.github.lombrozo.jsmith.antlr.view.TextTree;
import java.util.logging.Logger;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RandomScript}.
 * @since 0.1
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

    @RepeatedTest(10)
    void generatesLetterGrammarUsingCombinedGrammar() {
        final RandomScript script = new RandomScript(
            new ResourceOf("grammars/separated/LettersParser.g4"),
            new ResourceOf("grammars/separated/LettersLexer.g4")
        );
        this.logger.info(String.format("Letters spec (lisp format): %s", script.spec()));
        final String example = script.generate("sentences");
        this.logger.info(String.format("Generated Letters example:%n%s%n", example));
        MatcherAssert.assertThat(
            "We expect that the example for Letter grammar will be generated successfully and what is the most important - the grammar combined from two separate files - LettersLexer and LettersParser",
            example,
            Matchers.not(Matchers.emptyString())
        );
    }

    @RepeatedTest(10)
    void generatesWordsAndNumbersGrammarUsingCombinedGrammar() {
        final RandomScript script = new RandomScript(
            new ResourceOf("grammars/separated/WordsAndNumbersLexer.g4"),
            new ResourceOf("grammars/separated/WordsAndNumbersParser.g4")
        );
        this.logger.info(String.format("WordsAndNumbers spec (lisp format): %s", script.spec()));
        final String example = script.generate("words");
        this.logger.info(String.format("Generated WordsAndNumbers example:%n%s%n", example));
        MatcherAssert.assertThat(
            "We expect that the example for WordsAndNumbers grammar will be generated successfully and what is the most important - the grammar combined from two separate files - WordsAndNumbersLexer and WordsAndNumbersParser",
            example,
            Matchers.not(Matchers.emptyString())
        );
    }

    @RepeatedTest(10)
    void generatesJsonGrammarSuccessfully() {
        final RandomScript script = new RandomScript(new ResourceOf("grammars/Json.g4"));
        this.logger.info(String.format("Json spec (lisp format): %s", script.spec()));
        final String example = script.generate("json");
        this.logger.info(String.format("Generated Json example:%n%s%n", example));
        MatcherAssert.assertThat(
            "We expect that the example for Json grammar will be generated successfully",
            example,
            Matchers.not(Matchers.emptyString())
        );
    }

    @RepeatedTest(10)
    void generatesXmlGrammarSuccessfully() {
        final RandomScript script = new RandomScript(
            new ResourceOf("grammars/separated/XMLLexer.g4"),
            new ResourceOf("grammars/separated/XMLParser.g4")
        );
        this.logger.info(String.format("XML spec (lisp format): %s", script.spec()));
        final String example = new TextTree(script.generateText("document")).output();
        this.logger.info(String.format("Generated XML example:%n%s%n", example));
        MatcherAssert.assertThat(
            "We expect that the example for XML grammar will be generated successfully and what is the most important - the grammar combined from two separate files - XMLLexer.g4 and XMLParser.g4",
            example,
            Matchers.not(Matchers.emptyString())
        );
    }
}