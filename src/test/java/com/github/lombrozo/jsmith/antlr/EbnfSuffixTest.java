package com.github.lombrozo.jsmith.antlr;

import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link EbnfSuffix}.
 *
 * @since 0.1
 */
final class EbnfSuffixTest {

    /**
     * Generates all possible combinations of the EBNF suffix.
     * @return Stream of arguments for {@link #generatesSimpleEbnfSuffix(String, String, String)} ()}
     */
    public static Stream<Arguments> combinations() {
        return Stream.of(
            Arguments.of("?", null, "?"),
            Arguments.of("?", "?", "??"),
            Arguments.of("*", null, "*"),
            Arguments.of("*", "?", "*?"),
            Arguments.of("+", null, "+"),
            Arguments.of("+", "?", "+?")
        );
    }

    @ParameterizedTest
    @MethodSource("combinations")
    void generatesSimpleEbnfSuffix(
        final String operation, final String question, final String expected
    ) {
        MatcherAssert.assertThat(
            "We expect that the EBNF suffix will be generated",
            new EbnfSuffix(operation, question).generate(),
            Matchers.equalTo(expected)
        );
    }

    @Test
    void throwsExceptionWhenOperationIsNull() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new EbnfSuffix(null).generate(),
            "We expect that an exception will be thrown when the operation is null"
        );
    }

}