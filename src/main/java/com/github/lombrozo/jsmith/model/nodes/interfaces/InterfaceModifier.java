package com.github.lombrozo.jsmith.model.nodes.interfaces;

import com.github.lombrozo.jsmith.model.nodes.Node;

//interfaceModifier
//        :	annotation
//        |	'public'
//        |	'protected'
//        |	'private'
//        |	'abstract'
//        |	'static'
//        |	'strictfp'
//        ;

public class InterfaceModifier implements Node {

    @Override
    public String produce() {
        return this.verify("public");
    }
}
