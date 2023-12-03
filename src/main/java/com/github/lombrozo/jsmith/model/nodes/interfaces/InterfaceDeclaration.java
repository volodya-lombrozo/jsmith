package com.github.lombrozo.jsmith.model.nodes.interfaces;

import com.github.lombrozo.jsmith.model.nodes.Node;

//interfaceDeclaration
//        :	normalInterfaceDeclaration
//        |	annotationTypeDeclaration
//        ;

public class InterfaceDeclaration implements Node {

    private NormalInterfaceDeclaration normalInterfaceDeclaration;

    public InterfaceDeclaration() {
        this.normalInterfaceDeclaration = new NormalInterfaceDeclaration();
    }

    public NormalInterfaceDeclaration getNormalInterfaceDeclaration() {
        return normalInterfaceDeclaration;
    }

    @Override
    public String produce() {
        return this.verify(this.normalInterfaceDeclaration.produce());
    }

}
