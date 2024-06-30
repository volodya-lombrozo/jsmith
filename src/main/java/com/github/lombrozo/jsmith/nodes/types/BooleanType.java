package com.github.lombrozo.jsmith.nodes.types;

public class BooleanType implements IUnannType {

    private final String type = "boolean";

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String produce() {
        return this.type;
    }
}
