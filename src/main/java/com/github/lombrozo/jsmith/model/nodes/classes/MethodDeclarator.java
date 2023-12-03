package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.identifiers.GenericIdentifier;

//methodDeclarator
//        :	Identifier '(' formalParameterList? ')' dims?
//        ;

public class MethodDeclarator implements Node{

    private GenericIdentifier identifier;

    MethodDeclarator() {
        this.identifier = new GenericIdentifier();
    }

    public GenericIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();
        b.append(identifier.produce());
        b.append("(");
        b.append(")");
        return this.verify(b.toString());
    }
}
