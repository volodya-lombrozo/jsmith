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
import com.github.lombrozo.jsmith.antlr.view.Labels;
import com.github.lombrozo.jsmith.antlr.view.Text;
import java.util.Collections;
import java.util.List;

public final class ErrorSnippet implements Snippet {

    private final Text text;

    /**
     * Constructor.
     * @param author Who writes the error.
     * @param text Error message.
     */
    public ErrorSnippet(final Rule author, final String text) {
        this(new ErrorText(author, text));
    }

    /**
     * Constructor.
     * @param text Error message.
     */
    public ErrorSnippet(final Text text) {
        this.text = text;
    }

    @Override
    public Attributes attributes() {
        return new Attributes();
    }

    @Override
    public Text text() {
        return this.text;
    }

    @Override
    public boolean isError() {
        return true;
    }

    /**
     * Error text.
     * @since 0.1
     */
    private static final class ErrorText implements Text {

        /**
         * Who writes the error.
         */
        private final Rule author;

        /**
         * Error message.
         */
        private final String message;

        /**
         * Constructor.
         * @param writer Who writes the error.
         * @param message Error message.
         */
        private ErrorText(final Rule writer, final String message) {
            this.author = writer;
            this.message = message;
        }

        @Override
        public Rule writer() {
            return this.author;
        }

        @Override
        public List<Text> children() {
            return Collections.emptyList();
        }

        @Override
        public String output() {
            return this.message;
        }

        @Override
        public Labels labels() {
            return new Labels();
        }
    }

}