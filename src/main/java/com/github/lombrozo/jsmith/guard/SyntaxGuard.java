package com.github.lombrozo.jsmith.guard;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.tools.ToolProvider;
import org.antlr.v4.Tool;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.cactoos.Input;
import org.cactoos.Scalar;
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
     * Temporary directory.
     * Where the generated ANTLR lexer and parser will be stored.
     */
    private final Path temp;

    /**
     * ANTLR grammar text.
     */
    private final String grammar;

    /**
     * Top rule name.
     */
    private final String top;

    private final Sticky<Environment> environment;

    /**
     * Constructor.
     * @param temp Temporary directory.
     * @param input ANTLR grammar input.
     * @param top Top rule name.
     */
    public SyntaxGuard(final Path temp, final Input input, final String top) {
        this(temp, new UncheckedText(new TextOf(input)).asString(), top);
    }

    /**
     * Constructor.
     * @param temp Temporary directory.
     * @param grammar ANTLR grammar text.
     * @param top Top rule name.
     */
    SyntaxGuard(final Path temp, final String grammar, final String top) {
        this(temp, grammar, top, SyntaxGuard.prestructor(temp, grammar));
    }

    private SyntaxGuard(
        final Path temp,
        final String grammar,
        final String top,
        final Sticky<Environment> environment
    ) {
        this.temp = temp;
        this.grammar = grammar;
        this.top = top;
        this.environment = environment;
    }

    /**
     * Verify the generated code.
     * Throws a runtime exception if the code is incorrect or contains syntax errors.
     * The same as {@link SyntaxGuard#verify(String)} but without checked exceptions.
     * @param code Generated code.
     */
    public void verifySilently(final String code) {
        try {
            this.verify(code);
        } catch (InvalidSyntax exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Verify the generated code.
     * @param code Generated code.
     * @throws InvalidSyntax If the code is incorrect or contains syntax errors.
     */
    public void verify(final String code) throws InvalidSyntax {
        try {
            final Environment env = new Unchecked<>(this.environment).value();
            final Lexer lexer = this.lexer(env.lexer, code);
            final Parser parser = this.parser(env.parser, lexer);
            final SyntaxErrorListener errors = new SyntaxErrorListener();
            parser.addErrorListener(errors);
            lexer.addErrorListener(errors);
            env.parser.getMethod(this.top).invoke(parser);
            errors.report();
        } catch (final NoSuchMethodException | IllegalAccessException |
                       InvocationTargetException exception) {
            throw new IllegalStateException(
                "Something went wrong during ANTLR grammar compilation",
                exception
            );
        }
    }

    /**
     * Create parser instance.
     * @param cparser Loaded parser class.
     * @param lexer Lexer instance.
     * @return Parser instance.
     */
    private Parser parser(final Class<?> cparser, final Lexer lexer) {
        try {
            final Constructor<?> constructor = cparser.getDeclaredConstructor(TokenStream.class);
            return (Parser) constructor.newInstance(new CommonTokenStream(lexer));
        } catch (final NoSuchMethodException | IllegalAccessException |
                       InvocationTargetException | InstantiationException exception) {
            throw new IllegalStateException(
                "Something went wrong during parser creation",
                exception
            );
        }
    }

    /**
     * Create lexer instance.
     * @param clexer Loaded lexer class.
     * @param code Code to parse.
     * @return Lexer instance.
     */
    private Lexer lexer(final Class<?> clexer, final String code) {
        try {
            final Constructor<?> constructor = clexer.getDeclaredConstructor(CharStream.class);
            return (Lexer) constructor.newInstance(CharStreams.fromString(code));
        } catch (final NoSuchMethodException | IllegalAccessException |
                       InvocationTargetException | InstantiationException exception) {
            throw new IllegalStateException(
                "Something went wrong during lexer creation",
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
    private Class<?> load(String name) {
        try {
            return new URLClassLoader(
                new URL[]{this.temp.toFile().toURI().toURL()}
            ).loadClass(name);
        } catch (final MalformedURLException | ClassNotFoundException exception) {
            throw new IllegalStateException(
                "Something went wrong during class loading",
                exception
            );
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

    private static Sticky<Environment> prestructor(final Path temp, final String grammar) {
        return new Sticky<>(
            new Synced<>(SyntaxGuard.scalar(temp, grammar))
        );
    }

    private static Scalar<Environment> scalar(final Path temp, final String grammar) {
        return () -> {
            try {
                final String name = SyntaxGuard.grammarName(grammar);
                final Path gpath = temp.resolve(String.format("%s.g4", name));
                Files.write(gpath, grammar.getBytes(StandardCharsets.UTF_8));
                final Tool tool = new Tool(new String[]{gpath.toString()});
                tool.processGrammarsOnCommandLine();
                ToolProvider.getSystemJavaCompiler().run(
                    System.in,
                    System.out,
                    System.err,
                    Files.list(temp).filter(Files::isRegularFile)
                        .filter(java -> java.getFileName().toString().endsWith(".java"))
                        .map(Path::toString)
                        .toArray(String[]::new)
                );
                final Class<?> cparser = load(String.format("%sParser", name), temp);
                final Class<?> clexer = load(String.format("%sLexer", name), temp);
                return new Environment(clexer, cparser);
            } catch (final IOException exception) {
                throw new IllegalStateException(
                    "Something went wrong during ANTLR grammar compilation",
                    exception
                );
            }
        };
    }

    private static class Environment {

        private final Class<?> lexer;
        private final Class<?> parser;

        public Environment(final Class<?> lexer, final Class<?> parser) {
            this.lexer = lexer;
            this.parser = parser;
        }

        public Class<?> getLexer() {
            return this.lexer;
        }

        public Class<?> getParser() {
            return this.parser;
        }
    }
}