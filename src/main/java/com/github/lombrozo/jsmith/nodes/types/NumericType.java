package com.github.lombrozo.jsmith.nodes.types;

import com.github.lombrozo.jsmith.utils.RandomGen;

public class NumericType implements INumericType {

    private final INumericType type;

    NumericType() {
        if (RandomGen.getNextInt(2) == 0) {
            this.type = new IntegralType();
        } else {
            this.type = new FloatingPointType();
        }
    }

    @Override
    public String produce() {
        return this.type.produce();
    }

    @Override
    public String getType() {
        return this.type.getType();
    }
}
