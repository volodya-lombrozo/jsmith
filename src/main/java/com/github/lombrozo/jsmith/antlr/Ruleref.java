package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Unparser;

/**
 * Parser rule reference.
 * The rule definition in ANTLR grammar:
 * {@code
 * ruleref
 *     : RULE_REF argActionBlock? elementOptions?
 *     ;
 * }
 */
//@todo #1:Add more test cases for rule ref.
//  Currently we handle only the case when the rule reference is a simple string.
//  We need to add more test cases to cover the cases with argActionBlock and elementOptions.
public final class Ruleref implements Generative {

    private final Generative parent;
    private final String ref;
    private final Unparser unparser;

    public Ruleref(
        final Generative parent,
        final String ref,
        final Unparser unparser
    ) {
        this.parent = parent;
        this.ref = ref;
        this.unparser = unparser;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        try {
            return this.unparser.generate(this.ref);
        } catch (final RecursionException exception) {
            throw new IllegalStateException(
                String.format(
                    "Recursion detected in rule reference %s",
                    new ProductionsChain(this).toTree()
                ),
                exception
            );

        }
    }

    @Override
    public void append(final Generative generative) {
        throw new UnsupportedOperationException("Reference cannot have children yet");
    }

    @Override
    public String toString() {
        return String.format("ruleref(%s)", this.ref);
    }
}
