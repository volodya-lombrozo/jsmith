package com.github.lombrozo.jsmith.model.nodes.classes;

//variableDeclaratorId
//        :	Identifier dims?
//        ;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.identifiers.GenericIdentifier;

public class VariableDeclaratorId implements Node {
    private GenericIdentifier genericIdentifier;

    VariableDeclaratorId() {
        this.genericIdentifier = new GenericIdentifier();
    }

    public String getId() {
        return genericIdentifier.toString();
    }

    @Override
    public String produce() {
        return this.verify(this.genericIdentifier.produce());
    }
}
