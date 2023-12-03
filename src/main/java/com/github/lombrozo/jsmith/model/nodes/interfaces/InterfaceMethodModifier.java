package com.github.lombrozo.jsmith.model.nodes.interfaces;

import com.github.lombrozo.jsmith.model.nodes.Node;

//interfaceMethodModifier
//        :	annotation
//        |	'public'
//        |	'abstract'
//        |	'default'
//        |	'static'
//        |	'strictfp'
//        ;

public class InterfaceMethodModifier implements Node{
    @Override
    public String produce() {
        return this.verify("public");
    }
}
