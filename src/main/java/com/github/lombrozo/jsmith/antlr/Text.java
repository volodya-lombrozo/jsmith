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
package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.stream.Collector;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Text representation.
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
public final class Text {

    /**
     * Delimiter.
     */
    private static final String DELIMITER = " ";

    /**
     * Who writes the text.
     */
    private final Rule writer;

    /**
     * Generated text.
     */
    private final String output;

    public Text(final Rule writer, final String output) {
        this.writer = writer;
        this.output = output;
    }

    public String output() {
        return this.output;
    }

    public static Collector<Text, ?, String> joining() {
        return Collector.of(
            StringBuilder::new,
            (sb, text) -> sb.append(text.output()),
            (sb1, sb2) -> sb1.append(Text.DELIMITER).append(sb2),
            StringBuilder::toString
        );
    }
}
