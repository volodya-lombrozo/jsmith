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

import com.mifmif.common.regex.Generex;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a lexer element.
 * lexerElement
 *     : {@link LexerAtom} ebnfSuffix?
 *     | {@link LexerBlock} ebnfSuffix?
 *     | {@link ActionBlock} QUESTION?
 *     ;
 */
public final class LexerElement implements RuleDefinition {

    /**
     * Parent rule.
     */
    private final RuleDefinition parent;

    /**
     * Children rules.
     */
    private final List<RuleDefinition> children;

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LexerElement(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param parent Parent rule.
     * @param children Children rules.
     */
    private LexerElement(final RuleDefinition parent, final List<RuleDefinition> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (this.children.get(0) instanceof LexerAtom) {
            final String res;
            final RuleDefinition atom = this.children.get(0);
            final String generate = atom.generate();
            if ("\\r".equals(generate) || "\\n".equals(generate)) {
                //todo: You need to do something with special characters!
                return "\n";
            }
            if (this.children.size() > 1) {
                final RuleDefinition ebnSuffix = this.children.get(1);
                if (ebnSuffix != null) {
                    if (ebnSuffix instanceof EbnfSuffix) {
                        res = LexerElement.fromRegex(
                            String.format("%s%s", generate, ebnSuffix.generate())
                        );
                    } else {
                        throw new IllegalStateException(
                            String.format(
                                "The second element should be EbnfSuffix! But was %s, %s",
                                ebnSuffix.getClass().getSimpleName(),
                                ebnSuffix
                            )
                        );
                    }
                } else {
                    res = LexerElement.fromRegex(generate);
                }
            } else {
                res = generate;
            }
            return res;
        } else {
            return this.children.stream()
                .map(RuleDefinition::generate)
                .collect(Collectors.joining(" "));
        }
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.children.add(rule);
    }

    @Override
    public String toString() {
        return "lexerElement";
    }

    /**
     * Generate random string from regex.
     * @param regex Regex.
     * @return Random string.
     */
    private static String fromRegex(final String regex) {
        return new Generex(regex).random();
    }
}
