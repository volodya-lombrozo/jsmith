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
package com.github.lombrozo.jsmith.it;

import com.github.lombrozo.jsmith.RandomScript;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.guard.IllegalTextException;
import com.github.lombrozo.jsmith.guard.SyntaxGuard;
import com.jcabi.log.Logger;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.cactoos.Input;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * This test checks how we generate syntax for different grammars.
 * Those tests are slow and require a lot of resources.
 * @since 0.1
 */
final class SyntaxGenerationIT {

    /**
     * Provides grammar files and top rules.
     * @return Stream of arguments.
     * @todo #1:90min Add XML Grammar Verification
     *  Currently this grammar is disabled because we don't know what to do
     *  with skipped tokens like spaces and new lines. If we use space as a
     *  separator, we will have a lot of false positives. We need to find a
     *  way to verify XML grammar.
     *  To add the grammar use this code:
     *  Arguments.of(Arrays.asList(
     *  "grammars/separated/XMLLexer.g4",
     *  "grammars/separated/XMLParser.g4"
     *  ), "document")
     */
    static Stream<Arguments> syntax() {
        return Stream.of(
            Arguments.of(Collections.singletonList("grammars/Simple.g4"), "expr"),
            Arguments.of(Collections.singletonList("grammars/Arithmetic.g4"), "prog"),
            Arguments.of(Collections.singletonList("grammars/labeled/Arithmetic.g4"), "prog"),
            Arguments.of(Collections.singletonList("grammars/labeled/Assignments.g4"), "prog"),
            Arguments.of(Collections.singletonList("grammars/Recursive.g4"), "expr"),
            Arguments.of(Collections.singletonList("grammars/Json.g4"), "json"),
            Arguments.of(Collections.singletonList("grammars/CSV.g4"), "csvFile"),
            Arguments.of(Collections.singletonList("grammars/http.g4"), "http_message")
        );
    }

    @ParameterizedTest(name = "Generates programs for {0} grammar with top rule {1}")
    @MethodSource("syntax")
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void generatesSyntaxForGrammar(
        final List<String> definitions,
        final String top,
        @TempDir final Path temp
    ) {
        final Input[] grammars = definitions.stream().map(ResourceOf::new)
            .toArray(Input[]::new);
        final SyntaxGuard guard = new SyntaxGuard(temp, top, grammars);
        final String message =
            "We expect that the randomly generated code will be verified without errors";
        try {
            Assertions.assertDoesNotThrow(
                () -> Stream.generate(() -> top)
                    .peek(this::logStart)
                    .map(rule -> new RandomScript(grammars).generate(rule))
                    .limit(50)
                    .peek(this::logProgram)
                    .forEach(guard::verifySilently),
                message
            );
            Logger.debug(this, "Programs were generated and verified without errors");
        } catch (final IllegalTextException exception) {
            exception.saveDot();
            Assertions.fail(message, exception);
        }
    }

    /**
     * Logs the start of the generation.
     * @param starting Starting rule.
     */
    private void logStart(final String starting) {
        Logger.info(this, String.format("Generating program for '%s' rule", starting));
    }

    /**
     * Logs the generated program.
     * @param program The generated program.
     */
    private void logProgram(final Text program) {
        Logger.info(this, String.format("Generated program: %n```%n%s%n```", program.output()));
    }
}
