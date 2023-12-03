package com.github.lombrozo.jsmith.model;

/**
 * This class is the entry of the ScopeTable
 */
public class STEntry {

    private String type;
    private String identifier;
    private boolean _static;

    public STEntry(String type, String identifier, boolean _static) {
        this.type = type;
        this.identifier = identifier;
        this._static = _static;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isStatic() {
        return _static;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {

        return type + ", " + identifier +", "+ _static;
    }
}
