package com.github.lombrozo.jsmith.guard;

import com.github.lombrozo.jsmith.Randomizer;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * @since 0.1
 * @todo #1:90min Make SyntaxGuard cleaner!
 *  SyntaxGuard is too complex and uses java compiler to check the syntax of the code.
 *  We should find a way to verify the code without using compiler and any other dirty techniques
 *  as java reflection api. Also it would be nice to simplify the code and make it more readable.
 *  The starting point: <a href="https://stackoverflow.com/questions/5762067/in-antlr-3-how-do-i-generate-a-lexer-and-parser-at-runtime-instead-of-ahead-o">here.</a>
 */
public final class SyntaxGuard {


    private final Path temp;
    private final String grammar;

    private final String top;

    public SyntaxGuard(final Path temp, final Input input, final String top) {
        this(temp, new UncheckedText(new TextOf(input)).asString(), top);
    }

    public SyntaxGuard(final Path temp, final String grammar, final String top) {
        this.temp = temp;
        this.grammar = grammar;
        this.top = top;
    }

    public void verify(final String code) throws InvalidSyntax {
        try {
            final Path grammarPath = temp.resolve(
                String.format("Simple.g4", new Randomizer().nextInt(Integer.MAX_VALUE))
            );
            Files.write(grammarPath, this.grammar.getBytes(StandardCharsets.UTF_8));
            System.out.println(String.format("Grammar file was created %s", grammarPath));
            final Tool tool = new Tool(new String[]{grammarPath.toString()});
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
            Class lexerClass = this.loadClass("SimpleLexer");
            final Constructor declaredConstructor = lexerClass.getDeclaredConstructor(
                CharStream.class);
            Lexer lexer = (Lexer) declaredConstructor.newInstance(CharStreams.fromString(code));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            final Class<?> parserClass = this.loadClass("SimpleParser");
            Constructor parserCTor = parserClass.getConstructor(TokenStream.class);
            Parser parser = (Parser) parserCTor.newInstance(tokens);
            final SyntaxErrorListener listener = new SyntaxErrorListener();
            parser.addErrorListener(listener);
            lexer.addErrorListener(listener);
            parserClass.getMethod(this.top).invoke(parser);
            listener.report();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | InstantiationException | IOException exception) {
            throw new IllegalStateException(
                "Something went wrong during ANTLR grammar compilation",
                exception
            );
        }
    }

    private Class<?> loadClass(String name) throws ClassNotFoundException, MalformedURLException {
        return new URLClassLoader(new URL[]{this.temp.toFile().toURI().toURL()}).loadClass(name);
    }

}
