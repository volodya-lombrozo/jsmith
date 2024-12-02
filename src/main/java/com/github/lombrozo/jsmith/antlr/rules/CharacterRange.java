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
import java.util.regex.Pattern;

/**
 * CharacterRange rule.
 * The ANTLR grammar definition:
 * {@code
 * characterRange
 *     : STRING_LITERAL RANGE STRING_LITERAL
 *     ;
 * }
 * @since 0.1
 */
public final class CharacterRange implements Rule, Negatable {

    /**
     * Redundant characters.
     */
    private static final Pattern REDUNDANT = Pattern.compile("[ ']");

    /**
     * Dots that separate the range.
     */
    private static final Pattern DOTS = Pattern.compile("\\.\\.");

    /**
     * Parent rule.
     */
    private final Rule parentr;

    /**
     * Range text.
     */
    private final String text;

    /**
     * Random generator.
     */
    private final Rand rand;

    /**
     * Constructor.
     * @param parentr Parent rule.
     * @param text Range text.
     * @param rand Random generator.
     */
    public CharacterRange(
        final Rule parentr,
        final String text,
        final Rand rand
    ) {
        this.parentr = parentr;
        this.text = text;
        this.rand = rand;
    }

    /**
     * Constructor.
     * @param text Range text.
     */
    CharacterRange(final String text) {
        this(new Root(), text);
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param text Range text.
     */
    private CharacterRange(final Rule parent, final String text) {
        this(parent, text, new Rand());
    }

    @Override
    public Rule parent() {
        return this.parentr;
    }

    @Override
    public Text generate(final Context context) {
        try {
            return this.tryGenerate();
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                String.format("Can't choose random character from '%s' range", this.text),
                exception
            );
        }
    }

    @Override
    public Text negate(final Context context) {
        throw new UnsupportedOperationException("CharacterRange cannot be negated yet");
    }

    @Override
    public void append(final Rule rule) {
        throw new UnsupportedOperationException("CharacterRange cannot have children yet");
    }

    @Override
    public String name() {
        return String.format("characterRange(%s)", this.text);
    }

    @Override
    public Rule copy() {
        return new CharacterRange(this.parentr, this.text, this.rand);
    }

    /**
     * Try to generate a random character from the range.
     * @return Random character.
     */
    private Text tryGenerate() {
        final String[] pair = CharacterRange.DOTS.split(
            CharacterRange.REDUNDANT.matcher(this.text).replaceAll("")
        );
        final int start;
        final int end;
        if (pair.length < 2) {
            start = CharacterRange.code(pair[0]);
            end = start;
        } else {
            start = CharacterRange.code(pair[0]);
            end = CharacterRange.code(pair[1]);
        }
        return new TextLeaf(
            this,
            String.valueOf(Character.toChars(this.rand.range(start, end)))
        );
    }

    /**
     * Convert a character to an int code.
     * Unicode's characters are supported.
     * Several examples:
     * a->97, A->65, 1->49, !->33, 😈->128520, \u00B7->183, \u203F->8255, \u2040->8262.
     * @param character Character.
     * @return Code.
     */
    private static int code(final String character) {
        return new UnicodeChar(character).chararcter();
    }
}
