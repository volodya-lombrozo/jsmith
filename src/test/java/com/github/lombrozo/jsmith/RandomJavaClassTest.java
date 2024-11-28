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
package com.github.lombrozo.jsmith;

import com.jcabi.log.Logger;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test cases for {@link RandomJavaClass}.
 * @since 0.1
 */
final class RandomJavaClassTest {

    @Test
    void retrievesTheSourceCode() {
        MatcherAssert.assertThat(
            "We expect that random class will generate at least something",
            new RandomJavaClass().src(),
            Matchers.not(Matchers.emptyString())
        );
    }

    @ParameterizedTest
    @MethodSource("programs")
    void createsCompilableJavaSourceCode(final String src, @TempDir final Path temp) {
        Logger.info(this, "Generated source code: %n%s%n", src);
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final int run = compiler.run(
            new ByteArrayInputStream(src.getBytes(StandardCharsets.UTF_8)),
            new BufferedOutputStream(System.out),
            new BufferedOutputStream(System.err),
            RandomJavaClassTest.saveJava(src, temp)
        );
        MatcherAssert.assertThat(
            "The generated source code should be compilable",
            run,
            Matchers.equalTo(0)
        );
    }

    /**
     * Save Java source code to the file.
     * @param src Java source code.
     * @param temp Temporary directory.
     * @return Path to the saved file.
     */
    static String saveJava(final String src, final Path temp) {
        try {
            final Path java = temp.resolve("RandomJavaClass.java");
            Files.write(java, src.getBytes(StandardCharsets.UTF_8));
            return java.toString();
        } catch (final IOException ex) {
            throw new IllegalStateException("Can't save Java source code", ex);
        }
    }

    /**
     * Generate random programs.
     * @return Stream of random programs.
     */
    static Stream<Arguments> programs() {
        return Stream.generate(RandomJavaClass::new)
            .map(RandomJavaClass::src)
            .limit(10)
            .map(Arguments::of);
    }
}
