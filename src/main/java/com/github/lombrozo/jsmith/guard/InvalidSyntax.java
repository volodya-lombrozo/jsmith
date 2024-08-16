package com.github.lombrozo.jsmith.guard;

/**
 * This exception is thrown when the syntax of the generated code is incorrect.
 * @since 0.1
 */
final class InvalidSyntax extends Exception {

    /**
     * Constructor.
     * @param msg Error message.
     */
    InvalidSyntax(final String msg) {
        super(msg);
    }

    /**
     * Constructor.
     * @param message Error message.
     * @param cause Cause of the error.
     */
    InvalidSyntax(final String message, final Throwable cause) {
        super(message, cause);
    }
}
