package com.github.lombrozo.jsmith.it;

import com.github.lombrozo.jsmith.RandomScript;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextLeaf;
import com.github.lombrozo.jsmith.guard.IllegalText;
import com.github.lombrozo.jsmith.guard.SyntaxGuard;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.cactoos.Input;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * This test checks how we generate syntax for different grammars.
 * Those tests are slow and require a lot of resources.
 *
 * @since 0.1
 */
final class SyntaxGenerationIT {

    /**
     * Provides grammar files and top rules.
     * @return Stream of arguments.
     */
    static Stream<Arguments> syntax() {
        return Stream.of(
            Arguments.of(Collections.singletonList("grammars/Simple.g4"), "expr"),
            Arguments.of(Collections.singletonList("grammars/Arithmetic.g4"), "prog"),
            Arguments.of(Collections.singletonList("grammars/Recursive.g4"), "expr"),
            Arguments.of(Collections.singletonList("grammars/Json.g4"), "json"),
            Arguments.of(Collections.singletonList("grammars/CSV.g4"), "csvFile"),
            Arguments.of(Collections.singletonList("grammars/http.g4"), "http_message"),
            Arguments.of(
                Arrays.asList(
                    "grammars/separated/XMLLexer.g4",
                    "grammars/separated/XMLParser.g4"
                ),
                "document"
            )
        );
    }

    @ParameterizedTest(name = "Generates programs for {0} grammar with top rule {1}")
    @MethodSource("syntax")
    void generatesSyntaxForGrammar(
        final List<String> definitions,
        final String top,
        @TempDir final Path temp
    ) {
        final Input[] grammars = definitions.stream().map(ResourceOf::new)
            .toArray(Input[]::new);
        final RandomScript script = new RandomScript(grammars);
        final SyntaxGuard guard = new SyntaxGuard(temp, top, grammars);
        try {
            Stream.generate(() -> top)
                .map(script::generateText)
                .limit(50)
                .peek(SyntaxGenerationIT::logProgram)
                .forEach(guard::verifySilently);
        } catch (final IllegalText exception) {
            exception.saveDot();
            Assertions.fail(
                "We expect that the randomly generated code will be verified without errors",
                exception
            );
        }
    }

    //todo: remove me
//http://lab.antlr.org
    @Test
    void verifyGeneratedResult(@TempDir Path tmp) {
        final String input = "GET //%EE/%EE%EE%EE/%EE%EE HTTP/7.6\n" +
            "2566:__\n" +
            "\n";

        final SyntaxGuard guard = new SyntaxGuard(
            tmp, "http_message", new ResourceOf("grammars/http.g4"));
        guard.verifySilently(new TextLeaf(new Root(), input));
    }

    /**
     * Logs the generated program.
     * @param program The generated program.
     */
    private static void logProgram(final Text program) {
        Logger.getLogger(SyntaxGenerationIT.class.getSimpleName())
            .info(String.format("Generated program: %n%s%n", program.output()));
    }
}
