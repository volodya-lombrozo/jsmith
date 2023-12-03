package com.github.lombrozo.jsmith.nodes.types;

import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.List;

public class FloatingPointType implements INumericType {
    private final String type;

    public FloatingPointType() {
        final List<String> possibleTypes = List.of("float", "double");
        this.type = possibleTypes.get(RandomGen.getNextInt(possibleTypes.size()));
    }

    @Override
    public String produce() {
        return this.type;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
