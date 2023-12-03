package com.github.lombrozo.jsmith.model.nodes.interfaces;

//normalInterfaceDeclaration
//        :	interfaceModifier* 'interface' Identifier typeParameters? extendsInterfaces? interfaceBody
//        ;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.identifiers.ClassIdentifier;

public class NormalInterfaceDeclaration implements Node{

    private InterfaceModifier interfaceModifier;
    private ClassIdentifier identifier;
    private InterfaceBody interfaceBody;

    NormalInterfaceDeclaration() {
        this.interfaceModifier = new InterfaceModifier();
        this.identifier = new ClassIdentifier("Interface");
        this.interfaceBody = new InterfaceBody();
    }

    public ClassIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public String produce() {
        return this.verify(this.interfaceModifier.produce() + " interface " + this.identifier.produce() + this.interfaceBody.produce());
    }
}
