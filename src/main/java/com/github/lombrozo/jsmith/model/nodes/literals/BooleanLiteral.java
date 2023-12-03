package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.utils.RandomGen;

//BooleanLiteral
//        :	'true'
//        |	'false'
//        ;

public class BooleanLiteral implements ILiteral {

    private String boo;

    BooleanLiteral() {
        if (RandomGen.getNextInt(2) == 0) {
            this.boo = "true";
        } else {
            this.boo = "false";
        }

    }

    @Override
    public String produce() {
        return this.verify(boo);
    }
}
