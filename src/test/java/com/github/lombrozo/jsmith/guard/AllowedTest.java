/*
 * MIT License
 *
 * Copyright (c) 2023-2025 Volodya Lombrozo
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
package com.github.lombrozo.jsmith.guard;

import com.github.lombrozo.jsmith.antlr.rules.Literal;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Allowed}.
 *
 * @since 0.2.0
 */
final class AllowedTest {
    @Test
    void checksSuccess() {
        Assertions.assertDoesNotThrow(
            () -> new Allowed("TEST", "TEST1").check(new Literal("TEST1")),
            "We expect Allowed class check method to return true"
        );
    }

    @Test
    void checksThrow() {
        MatcherAssert.assertThat(
            "We expect correct error message",
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Allowed("FIRST, SECOND").check(new Literal("THIRD")),
                "We expect Allowed class check method to throw illegal argument"
            ).getMessage(),
            Matchers.equalTo("Rule literal(THIRD) can't be appended. Supported rules are : FIRST, SECOND")
        );
    }
}
