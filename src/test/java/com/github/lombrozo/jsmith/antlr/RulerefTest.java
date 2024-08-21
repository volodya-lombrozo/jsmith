package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Unparser;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Ruleref}.
 *
 * @since 1.0
 */
class RulerefTest {

    @Test
    void generatesRuleReferenceByUsingLink() {
        final String ref = "reference";
        final String expected = "Linked rule value";
        final ParserRuleSpec rule = new ParserRuleSpec(ref, new Generative.Empty());
        rule.append(new Literal(expected));
        final Unparser unparser = new Unparser();
        unparser.withParserRule(rule);
        final Ruleref ruleref = new Ruleref(new Generative.Empty(), ref, unparser);
        MatcherAssert.assertThat(
            String.format(
                "We expect that %s will invoke the linked rule to generate output, but it didn't happen",
                ruleref
            ),
            ruleref.generate(),
            Matchers.equalTo(expected)
        );
    }

    @Test
    void detectsRecursion() {
        final String ref = "reference";
        final Unparser unparser = new Unparser();
        final ParserRuleSpec rule = new ParserRuleSpec(ref, new Generative.Empty());
        final Generative recursive = new Ruleref(new Generative.Empty(), ref, unparser);
        rule.append(recursive);
        unparser.withParserRule(rule);
        Assertions.assertThrows(
            RecursionException.class,
            recursive::generate,
            String.format(
                "We expect that %s will throw an exception because of recursion, but it didn't happen",
                recursive
            )
        );
    }

}