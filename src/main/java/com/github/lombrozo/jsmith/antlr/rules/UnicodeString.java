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

final class UnicodeString {

    /**
     * Unicode pattern.
     */
    private static final Pattern UNICODE = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

    /**
     * Special characters pattern.
     */
    private static final Pattern SPECIAL = Pattern.compile("\\\\([nrtbf\"'\\\\])");

    private final String text;

    public UnicodeString(String text) {
        this.text = text;
    }

    public String asString() {
        return UnicodeString.replaceEscapes(
            UnicodeString.withoutApostrophes(
                UnicodeString.unescapeUnicodes(this.text)
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
        if (text.startsWith("'") && text.endsWith("'")) {
            result = text.substring(1, text.length() - 1);
        } else {
            result = text;
        }
        return result;
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
            final String result;
            if (original.replaceAll("'", "").startsWith("\\u")) {
                result = new UnicodeChar(original.replaceAll("'", "")).unescaped();
            } else {
                result = UnicodeString.tryToReplaceEscapes(original);
            }
            return result;
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                String.format("Failed to replace escape sequences in '%s'", original),
                exception
            );
        }
    }

    /**
     * Replace escape sequences in the string.
     * @param raw String with escape sequences.
     * @return String with replaced escape sequences.
     */
    private static String unescapeUnicodes(final String raw) {
        final Matcher matcher = UNICODE.matcher(raw);
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
     * Try to replace escape sequences.
     * @param original Original string.
     * @return String with replaced escape sequences.
     */
    private static String tryToReplaceEscapes(final String original) {
        final Matcher matcher = UnicodeString.SPECIAL.matcher(original);
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
