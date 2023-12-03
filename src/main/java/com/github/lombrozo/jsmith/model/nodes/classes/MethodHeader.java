package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.nodes.Node;

//methodHeader
//        :	result methodDeclarator throws_?
//        |	typeParameters annotation* result methodDeclarator throws_?
//        ;

public class MethodHeader implements Node{

    private Result result;
    private MethodDeclarator methodDeclarator;

    public MethodHeader() {
        this.result = new Result();
        this.methodDeclarator = new MethodDeclarator();
    }

    public Result getResult() {
        return result;
    }

    public MethodDeclarator getMethodDeclarator() {
        return methodDeclarator;
    }

    @Override
    public String produce() {
        String b = result.produce() +
                " " +
                methodDeclarator.produce();
        return this.verify(b);
    }
}
