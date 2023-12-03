package com.github.lombrozo.jsmith.model.nodes.literals;

//IntegerLiteral
//        :	DecimalIntegerLiteral
//        |	HexIntegerLiteral
//        |	OctalIntegerLiteral
//        |	BinaryIntegerLiteral
//        ;

import com.github.lombrozo.jsmith.model.STKey;

public class IntegerLiteral implements ILiteral {

    private DecimalIntegerLiteral decimalIntegerLiteral;

    IntegerLiteral(STKey key) {
        this.decimalIntegerLiteral = new DecimalIntegerLiteral(key);
    }

    @Override
    public String produce() {
        return this.verify(decimalIntegerLiteral.produce());
    }
}
