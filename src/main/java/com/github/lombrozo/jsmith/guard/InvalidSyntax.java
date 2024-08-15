package com.github.lombrozo.jsmith.guard;

/**
 * This exception is thrown when the syntax of the generated code is incorrect.
 * @since 0.1
 */
public final class InvalidSyntax extends Exception {

    /**
     * Constructor.
     * @param message Error message.
     */
    public InvalidSyntax(final String message) {
        super(message);
    }
}
