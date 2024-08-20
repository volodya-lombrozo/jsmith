package com.github.lombrozo.jsmith.antlr;

public final class RecursionException extends RuntimeException {

    public RecursionException(final String message) {
        super(message);
    }

    public RecursionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
