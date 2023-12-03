package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.nodes.Node;

//DecimalIntegerLiteral
//        :	DecimalNumeral IntegerTypeSuffix?
//        ;

public class DecimalIntegerLiteral implements Node {

    private DecimalNumeral decimalNumeral;
    private IntegerTypeSuffix integerTypeSuffix;

    DecimalIntegerLiteral(STKey key) {
        this.decimalNumeral = new DecimalNumeral(key);
        this.integerTypeSuffix = new IntegerTypeSuffix(key);
    }

    @Override
    public String produce() {
        return this.verify(decimalNumeral.produce() + integerTypeSuffix.produce());
    }
}
