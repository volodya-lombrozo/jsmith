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
        this.temp = temp;
        this.grammar = grammar;
        this.top = top;
    }

    /**
     * Verify the generated code.
     * @param code Generated code.
     * @throws InvalidSyntax If the code is incorrect or contains syntax errors.
     */
    public void verify(final String code) throws InvalidSyntax {
        try {
            final String name = this.grammarName();
            final Path gpath = this.temp.resolve(String.format("%s.g4", name));
            Files.write(gpath, this.grammar.getBytes(StandardCharsets.UTF_8));
            final Tool tool = new Tool(new String[]{gpath.toString()});
            tool.processGrammarsOnCommandLine();
            ToolProvider.getSystemJavaCompiler().run(
                System.in,
                System.out,
                System.err,
                Files.list(this.temp).filter(Files::isRegularFile)
                    .filter(java -> java.getFileName().toString().endsWith(".java"))
                    .map(Path::toString)
                    .toArray(String[]::new)
            );
            final Class<?> clexer = this.load(String.format("%sLexer", name));
            final Class<?> cparser = this.load(String.format("%sParser", name));
            final Constructor declaredConstructor = clexer.getDeclaredConstructor(CharStream.class);
            Lexer lexer = (Lexer) declaredConstructor.newInstance(CharStreams.fromString(code));
            final Constructor parserCTor = cparser.getConstructor(TokenStream.class);
            final Parser parser = (Parser) parserCTor.newInstance(new CommonTokenStream(lexer));
            final SyntaxErrorListener errors = new SyntaxErrorListener();
            parser.addErrorListener(errors);
            lexer.addErrorListener(errors);
            cparser.getMethod(this.top).invoke(parser);
            errors.report();
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | InstantiationException | IOException exception) {
            throw new IllegalStateException(
                "Something went wrong during ANTLR grammar compilation",
                exception
            );
        }
    }

    /**
     * Get grammar name.
     * @return Grammar name.
     */
    String grammarName() {
        final Matcher matcher = Pattern.compile("grammar\\s+(\\w+);").matcher(this.grammar);
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
}