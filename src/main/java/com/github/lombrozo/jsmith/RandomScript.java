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

import com.github.lombrozo.jsmith.antlr.AntlrListener;
import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.Unlexer;
import com.github.lombrozo.jsmith.antlr.Unparser;
import com.github.lombrozo.jsmith.antlr.semantic.Variables;
import com.github.lombrozo.jsmith.antlr.view.Text;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cactoos.Input;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Random script generator.
 * This class represents a random script generator based on ANTLR grammar.
 * In other words, it consumes ANTLR grammar and generates random scripts based on it.
 * @since 0.1
 */
@SuppressWarnings("PMD.ConstructorShouldDoInitialization")
public final class RandomScript {

    /**
     * ANTLR grammars.
     * They might be as standalone grammars or as separate lexer and parser grammars.
     */
    private final List<String> grammars;

    /**
     * Unlexer instance.
     */
    private final Unlexer unlexer = new Unlexer();

    /**
     * Unparser instance.
     */
    private final Unparser unparser = new Unparser();

    /**
     * Constructor.
     * @param grammars ANTLR grammars, either standalone or separate lexer and parser grammars.
     */
    public RandomScript(final Input... grammars) {
        this(
            Arrays.stream(grammars)
                .map(TextOf::new)
                .map(UncheckedText::new)
                .map(UncheckedText::asString)
                .collect(Collectors.toList())
        );
    }

    /**
     * Constructor.
     * @param grammars ANTLR grammars, either standalone or separate lexer and parser grammars.
     */
    private RandomScript(final List<String> grammars) {
        this.grammars = grammars;
    }

    /**
     * Generate random script text based on the starting rule.
     * @param rule Starting rule.
     * @return Random script text.
     */
    public Text generate(final String rule) {
        final Variables variables = new Variables();
        this.grammars.forEach(grammar -> this.parse(grammar, variables));
        return this.unparser.generate(rule, new Context());
    }

    /**
     * Simple ANTLR grammar specification in Lisp format.
     * @return ANTLR grammar specification in Lisp format.
     */
    String specification() {
        return this.grammars.stream()
            .map(RandomScript::parser)
            .map(parser -> parser.grammarSpec().toStringTree(parser))
            .collect(Collectors.joining("\n"));
    }

    /**
     * Parse ANTLR grammar.
     * @param grammar ANTLR grammar.
     * @param variables Variables.
     */
    private void parse(final String grammar, final Variables variables) {
        final ANTLRv4Lexer lexer = new ANTLRv4Lexer(CharStreams.fromString(grammar));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
        final ANTLRv4Parser.GrammarSpecContext spec = parser.grammarSpec();
        final ParseTreeWalker walker = new ParseTreeWalker();
        final AntlrListener listener = new AntlrListener(
            tokens,
            this.unparser,
            this.unlexer,
            variables
        );
        walker.walk(listener, spec);
    }

    /**
     * Create ANTLR parser.
     * @param grammar Antlr grammar.
     * @return ANTLR parser.
     */
    private static ANTLRv4Parser parser(final String grammar) {
        return new ANTLRv4Parser(
            new CommonTokenStream(new ANTLRv4Lexer(CharStreams.fromString(grammar)))
        );
    }
}
