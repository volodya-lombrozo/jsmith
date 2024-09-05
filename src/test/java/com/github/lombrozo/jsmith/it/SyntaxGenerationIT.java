package com.github.lombrozo.jsmith.it;

import com.github.lombrozo.jsmith.RandomScript;
import com.github.lombrozo.jsmith.guard.SyntaxGuard;
import java.nio.file.Path;
import java.util.logging.Logger;
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
            Arguments.of("grammars/Simple.g4", "expr"),
            Arguments.of("grammars/Arithmetic.g4", "prog"),
            Arguments.of("grammars/Recursive.g4", "expr"),
            Arguments.of("grammars/Json.g4", "json")
        );
    }

    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    //todo: Combined Grammars?
    @ParameterizedTest(name = "Generates programs for {0} grammar with top rule {1}")
    @MethodSource("syntax")
    void generatesSyntaxForGrammar(
        final String name,
        final String top,
        @TempDir final Path temp
    ) {
        final Input grammar = new ResourceOf(name);
        final RandomScript script = new RandomScript(grammar);
        final SyntaxGuard guard = new SyntaxGuard(temp, grammar, top);
        Assertions.assertDoesNotThrow(
            () -> Stream.generate(() -> top)
                .map(script::generate)
                .limit(50)
                .peek(SyntaxGenerationIT::logProgram)
                .forEach(guard::verifySilently),
            "We expect that the randomly generated code will be verified without errors"
        );
    }

    /**
     * Logs the generated program.
     * @param program The generated program.
     */
    private static void logProgram(final String program) {
        Logger.getLogger(SyntaxGenerationIT.class.getSimpleName())
            .info(String.format("Generated program: %n%s%n", program));
    }
}
