package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.nodes.classes.ClassDeclaration;

public class RandomJavaClass {

    private final ClassDeclaration classDeclaration;

    public RandomJavaClass() {
        this.classDeclaration = new ClassDeclaration();
    }

    public String name() {
        return classDeclaration.getClassIdentifier();
    }

    public String src() {
        return classDeclaration.produce();
    }

}
