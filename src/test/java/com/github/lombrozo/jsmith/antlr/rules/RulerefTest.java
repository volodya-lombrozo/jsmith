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
package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.antlr.Unparser;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Ruleref}.
 *
 * @since 1.0
 */
final class RulerefTest {

    @Test
    void generatesRuleReferenceByUsingLink() {
        final String ref = "reference";
        final String expected = "Linked rule value";
        final ParserRuleSpec rule = new ParserRuleSpec(ref, new Empty());
        rule.append(new Literal(expected));
        final Unparser unparser = new Unparser();
        unparser.with(ref, rule);
        final Ruleref ruleref = new Ruleref(new Empty(), ref, unparser);
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
        final ParserRuleSpec rule = new ParserRuleSpec(ref, new Empty());
        final Rule recursive = new Ruleref(new Empty(), ref, unparser);
        rule.append(recursive);
        unparser.with(ref, rule);
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> recursive.generate(),
            String.format(
                "We expect that %s will throw an exception because of recursion, but it didn't happen",
                recursive
            )
        );
    }

}