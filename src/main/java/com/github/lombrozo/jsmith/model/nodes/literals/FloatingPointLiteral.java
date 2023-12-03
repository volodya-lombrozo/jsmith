package com.github.lombrozo.jsmith.model.nodes.literals;

//FloatingPointLiteral
//        :	DecimalFloatingPointLiteral
//        |	HexadecimalFloatingPointLiteral
//        ;

import com.github.lombrozo.jsmith.model.STKey;

public class FloatingPointLiteral implements ILiteral {

    private DecimalFloatingPointLiteral decimalFloatingPointLiteral;

    FloatingPointLiteral(STKey key) {
        this.decimalFloatingPointLiteral = new DecimalFloatingPointLiteral(key);
    }

    @Override
    public String produce() {
        return this.verify(decimalFloatingPointLiteral.produce());
    }
}
