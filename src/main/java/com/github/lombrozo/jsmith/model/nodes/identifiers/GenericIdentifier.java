package com.github.lombrozo.jsmith.model.nodes.identifiers;

import com.github.lombrozo.jsmith.utils.RegexGen;

public class GenericIdentifier extends Identifier {

    private String genericIdentifier;

    public GenericIdentifier() {
        this.generex = new RegexGen("[A-Za-z][a-zA-Z0-9]{6,}");
        this.genericIdentifier = this.generex.get();
    }

    public String toString() {
        return this.genericIdentifier;
    }

    @Override
    public String produce() {
        return this.verify(this.genericIdentifier);
    }
}
