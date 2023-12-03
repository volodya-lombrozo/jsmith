package com.github.lombrozo.jsmith.nodes.types;

import com.github.lombrozo.jsmith.utils.RandomGen;

public class UnannPrimitiveType implements IUnannType {

    private final IUnannType type;

    public UnannPrimitiveType() {
        if (RandomGen.getNextInt(2) == 0) {
            this.type = new NumericType();
        } else {
            this.type = new BooleanType();
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
