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

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class.
 * Allowed checks rule in append method for correct rule appending.
 *
 * @since 0.2.0
 */
public class Allowed {
    /**
     * Allowed strings.
     */
    private final List<String> strings;

    /**
     * Constructor.
     * @param strings Strings to be added as allowed strings for class
     */
    public Allowed(final String... strings) {
        this.strings = Arrays.stream(strings).collect(Collectors.toList());
    }

    /**
     * Checks rule for certain class.
     * @param rule Rule to check
     * @return Boolean value indicating whether rule is suitable for appending.
     */
    public void check(final Rule rule) {
        boolean ret = false;
        for (final String allowed : this.strings) {
            if (rule.name().contains(allowed)) {
                ret = true;
                break;
            }
        }
        if (!ret) {
            throw new IllegalArgumentException(
                String.format(
                    "Rule %s can't be appended. Supported rules are : %s",
                    rule.name(), this.strings.stream().collect(Collectors.joining(", "))
                )
            );
        }
    }
}
