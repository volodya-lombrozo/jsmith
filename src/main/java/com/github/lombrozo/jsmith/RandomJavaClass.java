package com.github.lombrozo.jsmith;

public final class RandomJavaClass {

    private final String name;
    private final String source;

    public RandomJavaClass() {
        this("HelloWorld", RandomJavaClass.helloWorld());
    }

    public RandomJavaClass(final String name, final String source) {
        this.name = name;
        this.source = source;
    }

    public String name() {
        return this.name;
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
