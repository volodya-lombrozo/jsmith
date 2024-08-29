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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Literal rule.
 * The ANTLR grammar definition:
 * WARNING: This is NOT a part of the ANTLR grammar!
 * @since 0.1
 */
public final class Literal implements RuleDefinition {

    /**
     * Apostrophe pattern.
     */
    private static final Pattern APOSTROPHE = Pattern.compile("'", Pattern.LITERAL);

    /**
     * Special characters pattern.
     */
    private static final Pattern SPECIAL = Pattern.compile("\\\\([nrtbf\"'\\\\])");

    /**
     * Text of the literal.
     */
    private final String text;

    /**
     * Constructor.
     * @param text Text of the literal.
     */
    public Literal(final String text) {
        this.text = text;
    }

    @Override
    public RuleDefinition parent() {
        throw new UnsupportedOperationException("Literal cannot have parent");
    }

    @Override
    public String generate() {
        return Literal.APOSTROPHE.matcher(Literal.replaceEscapes(this.text)).replaceAll("");

    }

    @Override
    public void append(final RuleDefinition rule) {
        throw new UnsupportedOperationException("Literal cannot have children yet");
    }

    /**
     * Replace escape sequences.
     * For example:
     * {@code
     * \\n -> \n
     * \\r -> \r
     * \\t -> \t
     * ...
     * }
     * @param original Original string.
     * @return String with replaced escape sequences.
     */
    private static String replaceEscapes(final String original) {
        final Matcher matcher = Literal.SPECIAL.matcher(original);
        final StringBuffer result = new StringBuffer(original.length());
        while (matcher.find()) {
            final String replacement;
            switch (matcher.group(1)) {
                case "n":
                    replacement = "\n";
                    break;
                case "r":
                    replacement = "\r";
                    break;
                case "t":
                    replacement = "\t";
                    break;
                case "b":
                    replacement = "\b";
                    break;
                case "f":
                    replacement = "\f";
                    break;
                case "\\":
                    replacement = "\\";
                    break;
                case "\"":
                    replacement = "\"";
                    break;
                case "'":
                    replacement = "'";
                    break;
                default:
                    replacement = matcher.group(0);
            }
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    public String toString() {
        return String.format("literal(%s)", this.text);
    }
}