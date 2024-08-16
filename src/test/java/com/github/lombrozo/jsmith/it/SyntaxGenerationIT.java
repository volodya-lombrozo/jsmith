package com.github.lombrozo.jsmith.it;

import com.github.lombrozo.jsmith.Generator;
import com.github.lombrozo.jsmith.guard.SyntaxGuard;
import java.nio.file.Path;
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

    @ParameterizedTest(name = "Generates syntax for {1} with top rule {2}")
    @MethodSource("syntax")
    void generatesSyntaxForGrammar(
        final Path temp,
        final String name,
        final String top
    ) {
        final Input grammar = new ResourceOf(name);
        final String program = new Generator(grammar).generate(top);
        Assertions.assertDoesNotThrow(
            () -> new SyntaxGuard(temp, grammar, top).verify(program),
            "We expect that the randomly generated code will be verified without errors"
        );
    }

    /**
     * Provides grammar files and top rules.
     * @return Stream of arguments.
     */
    static Stream<Arguments> syntax(@TempDir final Path temp) {
        return Stream.of(
            Arguments.of(temp, "grammars/Simple.g4", "expr")
        );
    }
}
