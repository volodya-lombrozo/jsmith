package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//classBodyDeclaration
//        :	classMemberDeclaration
//        |	instanceInitializer
//        |	staticInitializer
//        |	constructorDeclaration
//        ;

public class ClassBodyDeclaration implements Node {

    private ClassMemberDeclaration classMemberDeclaration;

    ClassBodyDeclaration(String type, ScopeTable scopeTable) {
        this.classMemberDeclaration = new ClassMemberDeclaration(type, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(this.classMemberDeclaration.produce());
    }
}
