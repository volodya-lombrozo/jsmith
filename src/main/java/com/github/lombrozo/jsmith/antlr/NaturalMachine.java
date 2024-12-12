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
package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Params;
import com.github.lombrozo.jsmith.antlr.rules.LeftToRight;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.semantic.Scope;
import com.github.lombrozo.jsmith.antlr.view.SignedSnippet;
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.random.ConvergenceStrategy;
import com.github.lombrozo.jsmith.random.Rand;
import java.util.ArrayList;
import java.util.List;

/**
 * This machine travers generation tree and generates output.
 * @since 0.1
 */
public final class NaturalMachine {

    /**
     * Parameters.
     */
    private final Params params;
    /**
     * Root rule.
     */
    private final Rule root;

    public NaturalMachine(final Params params, final Rule root) {
        this.params = params;
        this.root = root;
    }

    public Text travers() {
        final Context context = new Context(
            new Scope(new Rand(this.params.seed())),
            new ConvergenceStrategy(this.params)
        );
        final List<Snippet> res = new ArrayList<>(0);
        this.travers(this.root, context, res);
        return new SignedSnippet(this.root, res).text();
    }

    void travers(final Rule rule, final Context context, final List<Snippet> res) {
        final List<Rule> children = rule.children(context);
        for (final Rule child : children) {
            final List<Rule> children1 = child.children(context);
            if (children1.isEmpty()) {
                res.add(child.generate(context));
            } else {
                this.travers(child, context, res);
            }
        }
    }

}
