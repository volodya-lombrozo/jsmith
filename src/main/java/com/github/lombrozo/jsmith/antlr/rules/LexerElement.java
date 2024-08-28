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

import com.github.lombrozo.jsmith.Rand;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a lexer element.
 * lexerElement
 *     : {@link LexerAtom} {@link EbnfSuffix}?
 *     | {@link LexerBlock} {@link EbnfSuffix}?
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
     * Random generator.
     */
    private final Rand rand;

    /**
     * Constructor.
     */
    public LexerElement() {
        this(new Root());
    }

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
        this.rand = new Rand();
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        final RuleDefinition first = this.children.get(0);
        if (first instanceof LexerAtom) {
            return this.multiplier().generate(first);
//            final String res;
//            final RuleDefinition atom = first;
//            final String generate = atom.generate();
//            if (this.children.size() > 1) {
//                final RuleDefinition ebnSuffix = this.children.get(1);
//                if (ebnSuffix != null) {
//                    if (ebnSuffix instanceof EbnfSuffix) {
//                        res = this.rand.regex(
//                            String.format("%s%s", generate, ebnSuffix.generate())
//                        );
//                    } else {
//                        throw new IllegalStateException(
//                            String.format(
//                                "The second element should be EbnfSuffix! But was %s, %s",
//                                ebnSuffix.getClass().getSimpleName(),
//                                ebnSuffix
//                            )
//                        );
//                    }
//                } else {
//                    res = this.rand.regex(generate);
//                }
//            } else {
//                res = generate;
//            }
//            return res;
        } else if (first instanceof LexerBlock) {
            return this.multiplier().generate(first);
        } else if (first instanceof ActionBlock) {
            return this.multiplier().generate(first);
        } else {
            throw new IllegalStateException(
                String.format("Unrecognized element type '%s' for '%s' element", first, this)
            );
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
     * Returns the multiplier for the element.
     * @return The multiplier for the {@link LexerAtom}, {@link ActionBlock} and {@link LexerBlock}.
     */
    private Multiplier multiplier() {
        final Multiplier result;
        if (this.children.size() == 1) {
            result = new Multiplier.One();
        } else {
            result = ((EbnfSuffix) this.children.get(1)).multiplier();
        }
        return result;
    }
}
