package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.antlr.Context;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * Tests for {@link RuleBlock}.
 *
 * @since 0.2.0
 */
final class RuleBlockTest {
    @Test
    void generatesRuleBlock() throws WrongPathException {
        final RuleBlock block = new RuleBlock(
            new Root(),
            new RuleAltList(
                new Root(),
                List.of(new Literal("Test1"), new Literal("Test2"))
            )
        );
        MatcherAssert.assertThat(
            "We expect RuleBlock to generate correctly",
            block.generate(new Context()).text().output(),
            Matchers.anyOf(
                Matchers.equalTo("Test1"),
                Matchers.equalTo("Test2")
            )
        );
    }

    @Test
    void throwsIllegalStateException() throws WrongPathException {
        final RuleBlock block = new RuleBlock(new Root(), new Literal("Invalid"));
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> block.generate(new Context()),
            "We expect ruleblock to throw exception if child is not RuleAltList class"
        );
    }
}
