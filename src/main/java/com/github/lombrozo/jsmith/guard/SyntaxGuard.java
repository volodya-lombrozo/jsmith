package com.github.lombrozo.jsmith.guard;

/**
 * This class is responsible for checking the syntax of the generated code.
 * @since 0.1
 */
public final class SyntaxGuard {

    private final String grammar;

    public SyntaxGuard(final String grammar) {
        this.grammar = grammar;
    }

    public void verify(final String code) throws InvalidSyntax {

    }

}
