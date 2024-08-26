package com.github.lombrozo.jsmith.antlr.rules;

/**
 * LabeledElement rule.
 * The ANTLR grammar definition:
 * {@code
 * labeledElement
 *     : {@link Identifier} (ASSIGN | PLUS_ASSIGN) ({@link Atom} | {@link Block})
 *     ;
 */
public final class LabeledElement extends Unimplemented {
    public LabeledElement(final RuleDefinition parent) {
        super(parent);
    }
}
