package com.github.lombrozo.jsmith.model.nodes.identifiers;

import com.github.lombrozo.jsmith.utils.RegexGen;

public class ClassIdentifier extends Identifier {

    private String identifier;

    private static final int IDENTIFIER_LENGTH = 5;

    public ClassIdentifier(String postfix) {
        this.generex = new RegexGen("[A-Z][a-zA-Z0-9]{" + IDENTIFIER_LENGTH + ",}");

        this.identifier = this.generex.get() + postfix;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String produce() {
        return this.verify(this.identifier);
    }
}
