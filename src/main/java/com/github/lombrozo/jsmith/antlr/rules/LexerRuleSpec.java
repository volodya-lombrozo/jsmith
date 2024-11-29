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
package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.antlr.Context;
import com.github.lombrozo.jsmith.antlr.view.RuleText;
import com.github.lombrozo.jsmith.antlr.view.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lexer rule specification.
 * The ANTLR rule definition:
 * {@code
 * lexerRuleSpec
 *     : FRAGMENT? TOKEN_REF {@link OptionsSpec}? COLON {@link LexerRuleBlock} SEMI
 *     ;
 * }
 * @since 0.1
 */
public final class LexerRuleSpec implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Rule name.
     */
    private final String alias;

    /**
     * Children rules.
     */
    private List<Rule> list;

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param name Rule name.
     */
    public LexerRuleSpec(final Rule parent, final String name) {
        this(parent, name, new ArrayList<>(0));
    }

    public LexerRuleSpec(final Rule top, final String alias, final List<Rule> list) {
        this.top = top;
        this.alias = alias;
        this.list = list;
    }

    /**
     * Constructor.
     * @param name Rule name.
     */
    LexerRuleSpec(final String name) {
        this(new Empty(), name);
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Text generate(final Context context) {
        return new RuleText(
            this,
            this.list.stream()
                .map(rule -> rule.generate(context))
                .collect(Collectors.toList())
        );
    }

    @Override
    public void append(final Rule rule) {
        this.list.add(rule);
    }

    @Override
    public String name() {
        return String.format("lexerRuleSpec(%s)", this.alias);
    }

    @Override
    public Rule copy() {
        return new LexerRuleSpec(
            this.top, this.alias, this.list.stream().map(Rule::copy).collect(Collectors.toList())
        );
    }
}
