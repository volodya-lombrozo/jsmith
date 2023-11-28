package com.github.lombrozo.jsmith;

public final class JavaProgram {

    private final String source;

    public JavaProgram() {
        this(JavaProgram.helloWorld());
    }

    public JavaProgram(final String src) {
        this.source = src;
    }

    public String src() {
        return this.source;
    }

    private static String helloWorld() {
        return String.join(
            "\n",
            "public class HelloWorld {",
            "    public static void main(String[] args) {",
            "        System.out.println(\"Hello, World!\");",
            "    }",
            "}"
        );
    }


}
