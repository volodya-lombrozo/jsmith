package com.github.lombrozo.jsmith.guard;

import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.antlr.view.TextTree;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.tools.ToolProvider;
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
 * @todo #1:90min Make SyntaxGuard cleaner!
 *  SyntaxGuard is too complex and uses java compiler to check the syntax of the code.
 *  We should find a way to verify the code without using compiler and any other dirty techniques
 *  as java reflection api. Also it would be nice to simplify the code and make it more readable.
 *  The starting point: <a href="https://stackoverflow.com/questions/5762067/in-antlr-3-how-do-i-generate-a-lexer-and-parser-at-runtime-instead-of-ahead-o">here.</a>
 */
public final class SyntaxGuard {

    /**
     * Pattern to check if the name has a suffix.
     */
    private static final Pattern HAS_SUFFIX = Pattern.compile("Lexer|Parser");

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
        } catch (final InvalidSyntax exception) {
            Logger.getLogger(SyntaxGuard.class.getSimpleName()).severe(
                String.format(
                    "Generated code tree: '%n%s%n' is wrong",
                    code.output()
                )
            );
            throw new IllegalText(
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
                    ToolProvider.getSystemJavaCompiler().run(
                        System.in,
                        System.out,
                        System.err,
                        Files.list(temp).filter(Files::isRegularFile)
                            .filter(java -> java.getFileName().toString().endsWith(".java"))
                            .map(Path::toString)
                            .toArray(String[]::new)
                    );
                    final String name = SyntaxGuard.HAS_SUFFIX.matcher(
                            SyntaxGuard.grammarName(grammars.get(0)))
                        .replaceAll("");
                    return new Environment(
                        SyntaxGuard.load(String.format("%sLexer", name), temp),
                        SyntaxGuard.load(String.format("%sParser", name), temp)
                    );
                }
            )
        );
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
                where.resolve(
                    String.format("%s.g4", SyntaxGuard.grammarName(grammar))),
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
     * @return Grammar name.
     */
    static String grammarName(final String grammar) {
        final Matcher matcher = Pattern.compile("grammar\\s+(\\w+);").matcher(grammar);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalStateException("Grammar name not found");
        }
    }

    /**
     * Load class from the temp directory.
     * @param name Class name.
     * @return Loaded class.
     */
    private static Class<?> load(String name, Path temp) {
        try {
            return new URLClassLoader(
                new URL[]{temp.toFile().toURI().toURL()}
            ).loadClass(name);
        } catch (final MalformedURLException | ClassNotFoundException exception) {
            throw new IllegalStateException(
                "Something went wrong during class loading",
                exception
            );
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
        private final Class<?> lexer;

        /**
         * Parser class.
         */
        private final Class<?> parser;

        /**
         * Constructor.
         * @param lexer Lexer class.
         * @param parser Parser class.
         */
        Environment(final Class<?> lexer, final Class<?> parser) {
            this.lexer = lexer;
            this.parser = parser;
        }

        /**
         * Create lexer instance.
         * @param code Code to parse.
         * @return Lexer instance.
         */
        Lexer lexer(final String code) {
            try {
                return (Lexer) this.lexer.getDeclaredConstructor(CharStream.class)
                    .newInstance(CharStreams.fromString(code));
            } catch (final NoSuchMethodException | IllegalAccessException |
                           InvocationTargetException | InstantiationException exception) {
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
                return (Parser) this.parser.getDeclaredConstructor(TokenStream.class)
                    .newInstance(new CommonTokenStream(lexer));
            } catch (final NoSuchMethodException | IllegalAccessException |
                           InvocationTargetException | InstantiationException exception) {
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
                this.parser.getMethod(top).invoke(parser);
            } catch (final NoSuchMethodException | IllegalAccessException |
                           InvocationTargetException exception) {
                throw new IllegalStateException(
                    "Something went wrong during parsing",
                    exception
                );
            }
        }
    }
}