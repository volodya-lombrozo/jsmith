package com.github.lombrozo.jsmith;

public class JavaProgram {

    private final String src;

    public JavaProgram(final String src) {
        this.src = src;
    }

    public String sourceCode() {
        return src;
    }

    private final String helloWorld(){
        return String.join(
            "\n",
            ""
        );
    }


}
