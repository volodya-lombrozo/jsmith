package com.github.lombrozo.jsmith.antlr;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AltList}.
 *
 * @since 0.1
 */
final class AltListTest {

    @Test
    void generatesOneOfTheAlternativesFromMany() {
        MatcherAssert.assertThat(
            "We expect that exactly one option will be chosen from the list of alternatives",
            new AltList(new RuleDefinition.Empty(), new Literal("1"), new Literal("2")).generate(),
            Matchers.either(Matchers.equalTo("1")).or(Matchers.equalTo("2"))
        );
    }

    @Test
    void generatesOneAlternativeFromSingle() {
        MatcherAssert.assertThat(
            "We expect that the only option will be chosen from the list of alternatives",
            new AltList(new RuleDefinition.Empty(), new Literal("1")).generate(),
            Matchers.equalTo("1")
        );
    }

    @Test
    void generatesNothing() {
        MatcherAssert.assertThat(
            "We expect that nothing will be generated",
            new AltList(new RuleDefinition.Empty()).generate(),
            Matchers.equalTo("")
        );
    }
}
