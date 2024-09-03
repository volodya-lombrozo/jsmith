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

    /**
     * Labeled element name.
     */
    private static final String NAME = "labeledElement";

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LabeledElement(final Rule parent) {
        super(parent);
    }

    @Override
    public String name() {
        return LabeledElement.NAME;
    }

    /**
     * Check if the rule is labeled element.
     * @param rule Rule.
     * @return True if the rule is labeled element.
     */
    public static boolean is(final Rule rule) {
        return LabeledElement.NAME.equals(rule.name());
    }
}
