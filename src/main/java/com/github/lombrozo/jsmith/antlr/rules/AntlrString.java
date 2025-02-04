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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String in ANTLR format.
 * This class can transform ANTLR string to the Java string.
 * For example:
 * {@code
 * 'Hello, world!' -> Hello, world!
 * 'Hello, \\u0077orld!' -> Hello, world!
 * }
 * In other words, it removes apostrophes and replaces escape sequences with unicode characters.
 * @since 0.1
 */
final class AntlrString {

    /**
     * Unicode pattern.
     */
    private static final Pattern UNICODE = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

    /**
     * Special characters pattern.
     */
    private static final Pattern SPECIAL = Pattern.compile("\\\\([nrtbf\"'\\\\])");

    /**
     * Original string in ANTLR format.
     */
    private final String original;

    /**
     * Constructor.
     * @param string Text.
     */
    AntlrString(final String string) {
        this.original = string;
    }

    /**
     * Transform ANTLR string to the Java string.
     * @return Java string.
     */
    String asString() {
        return AntlrString.replaceEscapes(
            AntlrString.withoutApostrophes(
                AntlrString.unescapeUnicodes(this.original)
            )
        );
    }

    /**
     * Remove apostrophes from the text.
     * @param text Text.
     * @return Text without apostrophes.
     */
    private static String withoutApostrophes(final String text) {
        final String result;
        if (!text.isEmpty() && text.charAt(0) == '\'' && text.charAt(text.length() - 1) == '\'') {
            result = text.substring(1, text.length() - 1);
        } else {
            result = text;
        }
        return result;
    }

    /**
     * Replace escape sequences in the string.
     * @param raw String with escape sequences.
     * @return String with replaced escape sequences.
     */
    private static String unescapeUnicodes(final String raw) {
        final Matcher matcher = AntlrString.UNICODE.matcher(raw);
        final StringBuffer res = new StringBuffer(0);
        while (matcher.find()) {
            matcher.appendReplacement(
                res,
                String.valueOf((char) Integer.parseInt(matcher.group(1), 16))
            );
        }
        matcher.appendTail(res);
        return res.toString();
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
        try {
            return AntlrString.tryToReplaceEscapes(original);
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                String.format("Failed to replace escape sequences in '%s'", original),
                exception
            );
        }
    }

    /**
     * Try to replace escape sequences.
     * @param original Original string.
     * @return String with replaced escape sequences.
     */
    private static String tryToReplaceEscapes(final String original) {
        final Matcher matcher = AntlrString.SPECIAL.matcher(original);
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
                case "\\\\":
                    replacement = "\\";
                    break;
                case "\\\"":
                    replacement = "\"";
                    break;
                case "\\'":
                    replacement = "'";
                    break;
                default:
                    replacement = matcher.group(0);
                    break;
            }
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
