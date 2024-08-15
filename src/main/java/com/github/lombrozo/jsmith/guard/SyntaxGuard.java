package com.github.lombrozo.jsmith.guard;

import com.github.lombrozo.jsmith.Randomizer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.transform.ErrorListener;
import org.antlr.v4.Tool;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.atn.ParseInfo;
import org.antlr.v4.runtime.dfa.DFA;
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

    public SyntaxGuard(final Path temp, final Input input) {
        this(temp, new UncheckedText(new TextOf(input)).asString());
    }


    public SyntaxGuard(final Path temp, final String grammar) {
        this.temp = temp;
        this.grammar = grammar;
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
//            final Method[] declaredMethods = parserClass.getDeclaredMethods();
//            parserClass.getMethod("expr").invoke(parser);
//            Method entryPointMethod = parserClass.getMethod("getNumberOfSyntaxErrors");
//            final Object invoke = entryPointMethod.invoke(parser);
//            System.out.println(invoke);
            final BaseErrorListener listener = new BaseErrorListener();
            parser.addErrorListener(listener);
            lexer.addErrorListener(new ANTLRErrorListener() {
                @Override
                public void syntaxError(
                    final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line,
                    final int charPositionInLine, final String msg, final RecognitionException e
                ) {
                    System.out.println("Syntax error: " + msg);
                }

                @Override
                public void reportAmbiguity(
                    final Parser recognizer, final DFA dfa, final int startIndex,
                    final int stopIndex, final boolean exact,
                    final BitSet ambigAlts, final ATNConfigSet configs
                ) {
                    System.out.println("Syntax error: " + code);

                }

                @Override
                public void reportAttemptingFullContext(
                    final Parser recognizer, final DFA dfa, final int startIndex,
                    final int stopIndex,
                    final BitSet conflictingAlts, final ATNConfigSet configs
                ) {
                    System.out.println("Syntax error: " + code);

                }

                @Override
                public void reportContextSensitivity(
                    final Parser recognizer, final DFA dfa, final int startIndex,
                    final int stopIndex, final int prediction,
                    final ATNConfigSet configs
                ) {
                    System.out.println("Syntax error: " + code);
                }
            });
            final String[] ruleNames = parser.getRuleNames();
            System.out.println(Arrays.toString(ruleNames));
            System.out.println(parser.getRuleInvocationStack());

            final Object invoke = parserClass.getMethod(ruleNames[0]).invoke(parser);
            System.out.println(invoke);
            System.out.println(parser.getNumberOfSyntaxErrors());

        } catch (Exception e) {
            throw new InvalidSyntax("Incorrect syntax", e);
        }
        System.out.println("Checking syntax of the code...");
    }

    private Class<?> loadClass(
        String className
    ) throws ClassNotFoundException, MalformedURLException {
        URL url = temp.toFile().toURI().toURL();          // file:/c:/myclasses/
        URL[] urls = new URL[]{url};

        // Create a new class loader with the directory
        ClassLoader cl = new URLClassLoader(urls);

        Class cls = cl.loadClass(className);
        return cls;
    }

}
