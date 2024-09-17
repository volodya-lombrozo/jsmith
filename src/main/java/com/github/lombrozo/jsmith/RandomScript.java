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

import com.github.lombrozo.jsmith.antlr.ANTLRListener;
import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.view.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cactoos.Input;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

public final class RandomScript {

    private final List<String> grammars;

    public RandomScript(final Input... grammars) {
        this(
            Arrays.stream(grammars)
                .map(TextOf::new)
                .map(UncheckedText::new)
                .map(UncheckedText::asString)
                .collect(Collectors.toList())
        );
    }

    public RandomScript(final List<String> grammars) {
        this.grammars = grammars;
    }

    public String generate(final String rule) {
        return this.generateText(rule).output();
    }

    public Text generateText(final String rule) {
        final ANTLRListener listener = new ANTLRListener();
        for (final String grammar : this.grammars) {
            new ParseTreeWalker().walk(listener, RandomScript.parser(grammar).grammarSpec());
        }
        return listener.unparser().generate(rule, new Context());
    }

    String spec() {
        List<String> res = new ArrayList<>(0);
        for (final String grammar : this.grammars) {
            final ANTLRv4Parser parser = RandomScript.parser(grammar);
            final ANTLRv4Parser.GrammarSpecContext spec = parser.grammarSpec();
            res.add(spec.toStringTree(parser));
        }
        return String.join("\n", res);
    }

    private static ANTLRv4Parser parser(final String grammar) {
        return new ANTLRv4Parser(
            new CommonTokenStream(
                new ANTLRv4Lexer(
                    CharStreams.fromString(grammar)
                )
            )
        );
    }
}
