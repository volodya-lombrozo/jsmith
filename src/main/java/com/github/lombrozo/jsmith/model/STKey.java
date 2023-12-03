package com.github.lombrozo.jsmith.model;

/**
 * This class is used by the model as the key of the ScopeTable class
 */
public class STKey {

    private String type;
    private boolean _static;

    public STKey(String type, boolean _static) {
        this.type = type;
        this._static = _static;
    }

    public String getType() {
        return type;
    }

    public boolean isStatic() {
        return _static;
    }
}
