package com.github.lombrozo.jsmith.model.nodes.interfaces;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.ArrayList;
import java.util.List;

//interfaceBody
//        :	'{' interfaceMemberDeclaration* '}'
//        ;

public class InterfaceBody implements Node {

    private List<InterfaceMemberDeclaration> interfaceMemberDeclarationList;

    private static final int MIN_NUMBER_OF_METHODS = 5;
    private static final int MAX_NUMBER_OF_METHODS = 9;

    InterfaceBody() {

        this.interfaceMemberDeclarationList = new ArrayList<>();

        for (int i = 0; i < RandomGen.getNextInt(MAX_NUMBER_OF_METHODS - MIN_NUMBER_OF_METHODS) + MIN_NUMBER_OF_METHODS; i++) {
            this.interfaceMemberDeclarationList.add(new InterfaceMemberDeclaration());
        }
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();
        for (InterfaceMemberDeclaration dec : this.interfaceMemberDeclarationList) {
            b.append(dec.produce());
        }

        return this.verify("{" + b.toString() + "}");
    }
}
