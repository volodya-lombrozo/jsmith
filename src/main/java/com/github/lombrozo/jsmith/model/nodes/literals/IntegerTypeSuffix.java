package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RegexGen;

public class IntegerTypeSuffix implements Node {

    private String suffix;

    IntegerTypeSuffix(STKey key) {
        if (!key.getType().equals("long")) {
            this.suffix = "";
        } else {
            suffix = (new RegexGen("[lL]")).get();
        }

    }

    @Override
    public String produce() {
        return this.verify(suffix);
    }
}
