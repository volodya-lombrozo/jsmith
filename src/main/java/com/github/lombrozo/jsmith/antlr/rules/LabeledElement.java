package com.github.lombrozo.jsmith.antlr.rules;

/**
 * LabeledElement rule.
 * The ANTLR grammar definition:
 * {@code
 * labeledElement
 *     : {@link Identifier} (ASSIGN | PLUS_ASSIGN) ({@link Atom} | {@link Block})
 *     ;
 * }
 * @since 0.1
 */
public final class LabeledElement extends Unimplemented {

    /**
     * Labeled element name.
     */
    private static final String ALIAS = "labeledElement";

    /**
     * Constructor.
     * @param parent Parent rule.
     */
    public LabeledElement(final Rule parent) {
        super(parent);
    }

    @Override
    public String name() {
        return LabeledElement.ALIAS;
    }

    /**
     * Check if the rule is labeled element.
     * @param rule Rule.
     * @return True if the rule is labeled element.
     */
    static boolean isLabeledElement(final Rule rule) {
        return LabeledElement.ALIAS.equals(rule.name());
    }
}
