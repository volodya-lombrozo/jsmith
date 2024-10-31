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

/**
 * Unicode character.
 * @since 0.1
 */
final class UnicodeChar {

    /**
     * Textual representation of the character.
     */
    private final String text;

    /**
     * Default constructor.
     * @param text Textual representation of the character
     */
    UnicodeChar(final String text) {
        this.text = text;
    }

    /**
     * Character code.
     * @return Character code
     */
    int chararcter() {
        final int result;
        if (this.text.startsWith("\\u")) {
            result = Integer.parseInt(this.text.substring(2), 16);
        } else {
            result = this.text.codePoints().sum();
        }
        return result;
    }

    /**
     * Unescaped character.
     * @return Unescaped character
     */
    String unescaped() {
        return String.valueOf(Character.toChars(this.chararcter()));
    }
}
