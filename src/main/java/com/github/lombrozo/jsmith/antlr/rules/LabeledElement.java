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
    private static final String KEY = "labeledElement";

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LabeledElement(final Rule parent) {
        super(parent);
    }

    @Override
    public String name() {
        return LabeledElement.KEY;
    }

    /**
     * Check if the rule is labeled element.
     * @param rule Rule.
     * @return True if the rule is labeled element.
     */
    static boolean isLabeledElement(final Rule rule) {
        return LabeledElement.KEY.equals(rule.name());
    }
}
