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

import com.github.lombrozo.jsmith.antlr.view.Text;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.antlr.v4.Tool;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.cactoos.Input;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Synced;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * This class is responsible for checking the syntax of the generated code.
 * It tries to generate default ANTLR lexer and parser and parse the code.
 * If the code is incorrect, it throws an exception.
 * @since 0.1
 * @todo #1:90min Make SyntaxGuard cleaner.
 *  SyntaxGuard is too complex and uses java compiler to check the syntax of the code.
 *  We should find a way to verify the code without using compiler and any other dirty techniques
 *  as java reflection api. Also it would be nice to simplify the code and make it more readable.
 *  The starting point: <a href="https://stackoverflow.com/questions/5762067/in-antlr-3-how-do-i-generate-a-lexer-and-parser-at-runtime-instead-of-ahead-o">here.</a>
 * @checkstyle IndentationCheck (500 lines)
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class SyntaxGuard {

    /**
     * Top rule name.
     */
    private final String top;

    /**
     * Environment with lexer and parser.
     * Created only once and then cached.
     */
    private final Sticky<Environment> environment;

    /**
     * Constructor.
     *
     * @param temp Temporary directory.
     * @param top Top rule name.
     * @param grammars ANTLR grammar input.
     */
    public SyntaxGuard(final Path temp, final String top, final Input... grammars) {
        this(
            temp,
            top,
            Arrays.stream(grammars)
                .map(TextOf::new)
                .map(UncheckedText::new)
                .map(UncheckedText::asString)
                .collect(Collectors.toList())
        );
    }

    /**
     * Constructor.
     *
     * @param temp Temporary directory.
     * @param top Top rule name.
     * @param grammar ANTLR grammar text.
     */
    SyntaxGuard(final Path temp, final String top, final List<String> grammar) {
        this(top, SyntaxGuard.prestructor(temp, grammar));
    }

    /**
     * Constructor.
     * @param top Top rule name.
     * @param environment Environment.
     */
    private SyntaxGuard(
        final String top,
        final Sticky<Environment> environment
    ) {
        this.top = top;
        this.environment = environment;
    }

    /**
     * Verify the generated code.
     * Throws a runtime exception if the code is incorrect or contains syntax errors.
     * The same as {@link SyntaxGuard#verify(String)} but without checked exceptions.
     * @param code Generated code.
     */
    public void verifySilently(final Text code) {
        try {
            this.verify(code.output());
            Logger.debug(this, "Generated code is correct");
        } catch (final InvalidSyntax exception) {
            Logger.error(
                this,
                String.format(
                    "Generated code tree: '%n%s%n' is wrong",
                    code.output()
                )
            );
            throw new IllegalTextException(
                String.format("Generated code '%s' is wrong", code.output()),
                code,
                exception
            );
        }
    }

    /**
     * Verify the generated code.
     * @param code Generated code.
     * @throws InvalidSyntax If the code is incorrect or contains syntax errors.
     */
    void verify(final String code) throws InvalidSyntax {
        final Environment env = new Unchecked<>(this.environment).value();
        final Lexer lexer = env.lexer(code);
        final Parser parser = env.parser(lexer);
        final SyntaxErrorListener errors = new SyntaxErrorListener();
        parser.addErrorListener(errors);
        lexer.addErrorListener(errors);
        env.parse(parser, this.top);
        errors.report();
    }

    /**
     * Prepare environment.
     * @param temp Temporary directory where to store generated classes.
     * @param grammars ANTLR grammar texts.
     * @return Environment that contains lexer and parser classes.
     */
    private static Sticky<Environment> prestructor(final Path temp, final List<String> grammars) {
        return new Sticky<>(
            new Synced<>(
                () -> {
                    new Tool(
                        grammars.stream()
                            .map(grammar -> SyntaxGuard.save(grammar, temp))
                            .toArray(String[]::new)
                    ).processGrammarsOnCommandLine();
                    final List<Class<?>> compiled = new InMemoryCompiler().compile(
                        Files.list(temp)
                            .filter(Files::isRegularFile)
                            .filter(java -> java.getFileName().toString().endsWith(".java"))
                            .toArray(Path[]::new)
                    );
                    final Class<?> lexer = SyntaxGuard.find(compiled, "Lexer");
                    final Class<?> parser = SyntaxGuard.find(compiled, "Parser");
                    return new Environment(lexer, parser);
                }
            )
        );
    }

    private static Class<?> find(final List<Class<?>> compiled, final String suffix) {
        return compiled.stream()
            .filter(clazz -> clazz.getName().endsWith(suffix))
            .findFirst()
            .orElseThrow();
    }

    /**
     * Save grammar to the file.
     * @param grammar Grammar text.
     * @param where Where to save.
     * @return Path to the saved grammar.
     */
    private static String save(final String grammar, final Path where) {
        try {
            return Files.write(
                where.resolve(String.format("%s.g4", SyntaxGuard.grammarName(grammar))),
                grammar.getBytes(StandardCharsets.UTF_8)
            ).toString();
        } catch (final IOException exception) {
            throw new IllegalStateException(
                "Something went wrong during grammar saving",
                exception
            );
        }
    }

    /**
     * Get grammar name.
     * @param grammar Grammar text.
     * @return Grammar name.
     */
    private static String grammarName(final String grammar) {
        final Matcher matcher = Pattern.compile("grammar\\s+(\\w+);").matcher(grammar);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalStateException("Grammar name not found");
        }
    }

    /**
     * Compiled lexer and parser classes.
     * This class encapsulates lexer and parser instances.
     * @since 0.1
     */
    private static class Environment {

        /**
         * Lexer class.
         */
        private final Class<?> clexer;

        /**
         * Parser class.
         */
        private final Class<?> cparser;

        /**
         * Constructor.
         * @param clexer Lexer class.
         * @param cparser Parser class.
         */
        Environment(final Class<?> clexer, final Class<?> cparser) {
            this.clexer = clexer;
            this.cparser = cparser;
        }

        /**
         * Create lexer instance.
         * @param code Code to parse.
         * @return Lexer instance.
         * @checkstyle CascadeIndentationCheck (50 lines)
         */
        Lexer lexer(final String code) {
            try {
                return (Lexer) this.clexer.getDeclaredConstructor(CharStream.class)
                    .newInstance(CharStreams.fromString(code));
            } catch (final Exception exception) {
                throw new IllegalStateException(
                    "Something went wrong during lexer creation",
                    exception
                );
            }
        }

        /**
         * Create parser instance.
         * @param lexer Lexer instance.
         * @return Parser instance.
         */
        Parser parser(final Lexer lexer) {
            try {
                return (Parser) this.cparser.getDeclaredConstructor(TokenStream.class)
                    .newInstance(new CommonTokenStream(lexer));
            } catch (final Exception exception) {
                throw new IllegalStateException(
                    "Something went wrong during parser creation",
                    exception
                );
            }
        }

        /**
         * Parse the code by using top rule.
         * @param parser Parser instance.
         * @param top Top rule name.
         */
        void parse(final Parser parser, final String top) {
            try {
                this.cparser.getMethod(top).invoke(parser);
            } catch (final Exception exception) {
                throw new IllegalStateException(
                    "Something went wrong during parsing",
                    exception
                );
            }
        }
    }


}
