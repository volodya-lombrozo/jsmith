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
import com.github.lombrozo.jsmith.antlr.view.Node;
import com.github.lombrozo.jsmith.antlr.view.TerminalNode;
import com.github.lombrozo.jsmith.random.Rand;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LexerCharSet rule.
 * @since 0.1
 */
public final class LexerCharSet implements Rule, Negatable {

    /**
     * Unicode pattern.
     */
    private static final Pattern UNICODE = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Char set.
     * Might be a range or a set of characters.
     */
    private final String text;

    /**
     * Random generator.
     */
    private final Rand rand;

    /**
     * Constructor.
     * @param text Char set that might be a range or a set of characters.
     */
    public LexerCharSet(final String text) {
        this(new Root(), text);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param text Text.
     */
    public LexerCharSet(final Rule parent, final String text) {
        this(parent, text, new Rand());
    }

    /**
     * Constructor.
     *
     * @param parent Parent rule.
     * @param text Text.
     * @param rand Random generator.
     */
    public LexerCharSet(final Rule parent, final String text, final Rand rand) {
        this.top = parent;
        this.text = text;
        this.rand = rand;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Node generate(final Context context) {
        return new TerminalNode(
            this,
            this.rand.regex(new UnicodeString(this.text).asString())
        );
    }

    @Override
    public Node negate(final Context context) {
        final String negated;
//        final String replaced = Literal.replaceEscapes(this.text);
        final String replaced = new UnicodeString(this.text).asString();
        if (!replaced.isEmpty() && replaced.charAt(0) == '[') {
            negated = String.format("%s^%s", replaced.charAt(0), replaced.substring(1));
        } else {
            negated = String.format("[^%s]", replaced);
        }
        return new TerminalNode(
            this,
            this.rand.regex(negated)
//            this.rand.regex(LexerCharSet.unescapeUnicodes(negated))
        );
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("LexerCharSet cannot have children");
    }

    @Override
    public String name() {
        return String.format("lexerCharSet('%s')", this.text);
    }

    @Override
    public Rule copy() {
        return new LexerCharSet(this.top, this.text, this.rand);
    }

    /**
     * Replace escape sequences in the string.
     * @param raw String with escape sequences.
     * @return String with replaced escape sequences.
     */
    private static String unescapeUnicodes(final String raw) {
        final Matcher matcher = LexerCharSet.UNICODE.matcher(raw);
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
}
