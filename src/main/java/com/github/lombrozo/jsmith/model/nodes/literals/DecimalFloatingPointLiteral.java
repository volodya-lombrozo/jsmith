package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.nodes.Node;

import java.util.Random;

//DecimalFloatingPointLiteral
//        :	Digits '.' Digits? ExponentPart? FloatTypeSuffix?
//        |	'.' Digits ExponentPart? FloatTypeSuffix?
//        |	Digits ExponentPart FloatTypeSuffix?
//        |	Digits FloatTypeSuffix
//        ;

public class DecimalFloatingPointLiteral implements Node{

    private String decimalFloatintPointLiteral;

    DecimalFloatingPointLiteral(STKey key) {
        Random random = new Random();
        switch (key.getType()) {
            case "double":
                this.decimalFloatintPointLiteral = String.valueOf(random.nextDouble());
                break;
            case "float":
                this.decimalFloatintPointLiteral = String.valueOf(random.nextFloat()) + new FloatTypeSuffix().produce();
                break;
        }
    }

    @Override
    public String produce() {
        return this.verify(decimalFloatintPointLiteral);
    }
}
