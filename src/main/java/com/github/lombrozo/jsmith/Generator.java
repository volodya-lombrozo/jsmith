/*
 * MIT License
 *
 * Copyright (c) 2023 Volodya Lombrozo
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
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;

public final class Generator {

    public void generate() {
        try {
            final String grammar = new TextOf(new ResourceOf("grammars/Simple.g4")).asString();
            final CodePointCharStream stream = CharStreams.fromString(grammar);
            final ANTLRv4Lexer lexer = new ANTLRv4Lexer(stream);
            final CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            final ANTLRv4Parser parser = new ANTLRv4Parser(commonTokenStream);
            final ANTLRv4Parser.GrammarSpecContext spec = parser.grammarSpec();
            final ParseTreeWalker walker = new ParseTreeWalker();
            final ANTLRListener listener = new ANTLRListener();
            final String stringTree = spec.toStringTree(parser);
            System.out.println(stringTree);
            walker.walk(listener, spec);
            System.out.println("Unparser:\n");
            final Unparser unparser = listener.unparser();
            System.out.println(unparser.generate());
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
