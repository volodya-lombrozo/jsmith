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
package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.antlr.view.Node;
import com.github.lombrozo.jsmith.antlr.view.PlainText;
import com.github.lombrozo.jsmith.antlr.view.TerminalNode;
import java.util.concurrent.CountDownLatch;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test case for {@link SeveralAttempts}.
 * @since 0.1
 */
final class SeveralAttemptsTest {

    @ParameterizedTest
    @CsvSource({"3, success", "4, success"})
    void choosesCorrectly(final int attempts, final String expected) throws WrongPathException {
        MatcherAssert.assertThat(
            String.format("We expect the %d attempt to be %s", attempts, expected),
            new SeveralAttempts(attempts, "test", new ThreeAttempts())
                .choose()
                .text()
                .output(),
            Matchers.containsString(expected)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void throwsErrorForTheFirstAttempts(final int attempts) {
        Assertions.assertThrows(
            WrongPathException.class,
            () -> new SeveralAttempts(attempts, "test", new ThreeAttempts())
                .choose()
                .text()
                .output(),
            String.format("We expect first %d attempts to be failed", attempts)
        );
    }

    @Test
    void failsForSingleAttempt() {
        Assertions.assertThrows(
            WrongPathException.class,
            () -> new SeveralAttempts(
                1,
                "test",
                () -> {
                    throw new WrongPathException("Mock failure");
                }
            ).choose().text().output(),
            "We expect that the exception will be thrown"
        );
    }

    /**
     * Mock text generation that starts to work only from the third attempt.
     * @since 0.1
     */
    private static final class ThreeAttempts implements SeveralAttempts.Attempt {

        /**
         * Attempts.
         */
        private final CountDownLatch attempts = new CountDownLatch(2);

        @Override
        public Node make() throws WrongPathException {
            final Node result;
            if (this.attempts.getCount() == 0) {
                result = new TerminalNode(new PlainText("success"));
                this.attempts.countDown();
            } else {
                this.attempts.countDown();
                throw new WrongPathException("Some error");
            }
            return result;
        }
    }
}
