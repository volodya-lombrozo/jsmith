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
import com.github.lombrozo.jsmith.antlr.view.NodeSnippet;
import com.github.lombrozo.jsmith.antlr.view.Snippet;
import com.github.lombrozo.jsmith.antlr.view.Labels;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser spec rule.
 * The rule definition in ANTLR grammar:
 * {@code
 * parserRuleSpec
 *     : ruleModifiers? RULE_REF {@link ArgActionBlock}? ruleReturns? \
 *     throwsSpec? localsSpec? rulePrequel* COLON {@link RuleBlock} SEMI
 *         exceptionGroup
 *     ;
 * }
 * @since 0.1
 */
public final class ParserRuleSpec implements Rule {

    /**
     * Parent rule.
     */
    private final Rule top;

    /**
     * Rule name.
     */
    private final String rname;

    /**
     * Children rules.
     */
    private final List<Rule> list;

    /**
     * Constructor.
     * @param name Rule name.
     * @param parent Parent rule.
     */
    public ParserRuleSpec(final String name, final Rule parent) {
        this(name, parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param name Rule name.
     * @param parent Parent rule.
     * @param list Children rules.
     */
    private ParserRuleSpec(final String name, final Rule parent, final List<Rule> list) {
        this.top = parent;
        this.rname = name;
        this.list = list;
    }

    @Override
    public Rule parent() {
        return this.top;
    }

    @Override
    public Snippet generate(final Context context) {
        return new NodeSnippet(
            this.list.stream()
                .map(rule -> rule.generate(context))
                .collect(Collectors.toList()),
            new Labels(this).rule(this.rname)
        );
    }

    @Override
    public void append(final Rule rule) {
        this.list.add(rule);
    }

    @Override
    public String name() {
        return String.format("parserRuleSpec(%s)", this.rname);
    }

    @Override
    public Rule copy() {
        return new ParserRuleSpec(
            this.rname,
            this.top,
            this.list.stream()
                .map(Rule::copy)
                .collect(Collectors.toList())
        );
    }
}
