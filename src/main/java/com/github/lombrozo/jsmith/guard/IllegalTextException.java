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

import com.github.lombrozo.jsmith.antlr.view.DotText;
import com.github.lombrozo.jsmith.antlr.view.RulesOnly;
import com.github.lombrozo.jsmith.antlr.view.Text;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * This exception is thrown when the text is incorrect.
 * @since 0.1
 */
public final class IllegalTextException extends IllegalStateException {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -3351992442473768318L;

    /**
     * Pattern for '.dot' file.
     */
    private static final Pattern DOT = Pattern.compile(".dot");

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SyntaxGuard.class.getSimpleName());

    /**
     * Illegal text.
     */
    private final Text origin;

    /**
     * Constructor.
     * @param message Error message.
     * @param output Illegal text.
     * @param cause Cause.
     */
    IllegalTextException(final String message, final Text output, final Throwable cause) {
        super(message, cause);
        this.origin = output;
    }

    /**
     * Save '.dot' file in case of syntax error.
     */
    public void saveDot() {
        try {
            final Path file = Files.createTempFile("jsmith-", ".dot");
            Files.write(
                file,
                new DotText(
                    this.origin,
                    new RulesOnly()
                ).output().getBytes(StandardCharsets.UTF_8)
            );
            IllegalTextException.LOG.severe(
                String.format("Dot file saved to: file://%s", file)
            );
            IllegalTextException.toSvg(file);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                "Something went wrong during '.dot' file saving",
                exception
            );
        }
    }

    /**
     * Convert '.dot' file to '.svg'.
     * @param file Dot '.dot' file.
     */
    private static void toSvg(final Path file) {
        try {
            final Path svg = file.getParent()
                .resolve(
                    IllegalTextException.DOT
                        .matcher(file.getFileName().toString())
                        .replaceAll(".svg")
                );
            final Process process = new ProcessBuilder(
                "dot", "-Tsvg", "-o",
                svg.toString(),
                file.toString()
            ).start();
            IllegalTextException.LOG.info(
                new UncheckedText(
                    new TextOf(
                        new InputStreamReader(
                            process.getInputStream(),
                            StandardCharsets.UTF_8
                        )
                    )
                ).asString()
            );
            IllegalTextException.LOG.severe(
                new UncheckedText(
                    new TextOf(
                        new InputStreamReader(
                            process.getErrorStream(),
                            StandardCharsets.UTF_8
                        )
                    )
                ).asString()
            );
            IllegalTextException.LOG.info(
                String.format("Process exited with code: %d", process.waitFor())
            );
            IllegalTextException.LOG.severe(String.format("SVG file saved to: file://%s", svg));
        } catch (final IOException | InterruptedException exception) {
            throw new IllegalStateException(
                "Something went wrong during '.svg' file generation",
                exception
            );
        }
    }
}
