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

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextLeaf;
import com.github.lombrozo.jsmith.random.Rand;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Literal rule.
 * The ANTLR grammar definition:
 * WARNING: This is NOT a part of the ANTLR grammar!
 * @since 0.1
 */
public final class Literal implements Rule, Negatable {

    /**
     * Apostrophe pattern.
     */
    private static final Pattern APOSTROPHE = Pattern.compile("'", Pattern.LITERAL);

    /**
     * Special characters pattern.
     */
    private static final Pattern SPECIAL = Pattern.compile("\\\\([nrtbf\"'\\\\])");

    /**
     * Parent rule.
     */
    private final Rule parent;

    /**
     * Text of the literal.
     */
    private final String text;

    /**
     * Constructor.
     * @param text Text of the literal.
     */
    public Literal(final String text) {
        this(new Root(), text);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param text Text of the literal.
     */
    public Literal(final Rule parent, final String text) {
        this.parent = parent;
        this.text = text;
    }

    @Override
    public Rule parent() {
        return this.parent;
    }

    @Override
    public Text generate(final Context context) {
        return new TextLeaf(
            this,
            Literal.APOSTROPHE.matcher(
                Literal.replaceEscapes(this.withoutApostrophes())
            ).replaceAll("")
        );
    }

    @Override
    public Text negate(final Context context) {
        return new TextLeaf(
            this,
            new Rand().regex(String.format("[^%s]", this.text))
        );
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("Literal cannot have children yet");
    }

    @Override
    public String name() {
        return String.format("literal(%s)", this.text);
    }

    @Override
    public String toString() {
        return this.name();
    }

    private String withoutApostrophes() {
        if (this.text.startsWith("'") && this.text.endsWith("'")) {
            return this.text.substring(1, this.text.length() - 1);
        } else {
            return this.text;
        }
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
     * @todo #1:90min Make it separate class.
     */
    public static String replaceEscapes(final String original) {
        try {
            if (original.replaceAll("'", "").startsWith("\\u")) {
                return new UnicodeChar(original.replaceAll("'", "")).unescaped();
            }
            return Literal.tryToReplaceEscapes(original);
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
                case "\\\\":
                    replacement = "\\";
                    break;
                case "\\\"":
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
}
