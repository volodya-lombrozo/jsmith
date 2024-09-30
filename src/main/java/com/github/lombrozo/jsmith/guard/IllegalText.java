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
package com.github.lombrozo.jsmith.guard;

import com.github.lombrozo.jsmith.antlr.view.DotTree;
import com.github.lombrozo.jsmith.antlr.view.Text;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * This exception is thrown when the text is incorrect.
 * @since 0.1
 */
public final class IllegalText extends RuntimeException {

    private final Text origin;

    /**
     * Constructor.
     * @param ouput Error message.
     */
    public IllegalText(final String message, final Text ouput, final Throwable cause) {
        super(message, cause);
        this.origin = ouput;
    }

    /**
     * Save '.dot' file in case of syntax error.
     */
    public void saveDot() {
        try {
            final Path file = Files.createTempFile("jsmith-", ".dot");
            Files.write(
                file,
                new DotTree(this.origin).output().getBytes(StandardCharsets.UTF_8)
            );
            Logger.getLogger(SyntaxGuard.class.getSimpleName()).severe(
                String.format("Dot file saved to: file://%s", file)
            );
        } catch (final IOException exception) {
            throw new IllegalStateException(
                "Something went wrong during '.dot' file saving",
                exception
            );
        }
    }
}
