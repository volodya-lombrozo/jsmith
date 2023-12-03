package com.github.lombrozo.jsmith.model.nodes.interfaces;

import com.github.lombrozo.jsmith.model.nodes.Node;

//interfaceMemberDeclaration
//        :	constantDeclaration
//        |	interfaceMethodDeclaration
//        |	classDeclaration
//        |	interfaceDeclaration
//        |	';'
//        ;

public class InterfaceMemberDeclaration implements Node{

    private InterfaceMethodDeclaration interfaceMethodDeclaration;

    InterfaceMemberDeclaration() {
        this.interfaceMethodDeclaration = new InterfaceMethodDeclaration();
    }

    @Override
    public String produce() {
        return this.verify(this.interfaceMethodDeclaration.produce());
    }
}
