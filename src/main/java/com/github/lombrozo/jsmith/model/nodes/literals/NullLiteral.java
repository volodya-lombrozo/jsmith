package com.github.lombrozo.jsmith.model.nodes.literals;

//NullLiteral
//        :	'null'
//        ;

public class NullLiteral implements ILiteral {
    @Override
    public String produce() {
        return "null";
    }
}
