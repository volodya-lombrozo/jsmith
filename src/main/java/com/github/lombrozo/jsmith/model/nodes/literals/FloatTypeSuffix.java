package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RegexGen;

//FloatTypeSuffix
//        :	[fFdD]
//        ;

public class FloatTypeSuffix implements Node{

    private String suffix;

    FloatTypeSuffix() {
        this.suffix = (new RegexGen("[fF]")).get();
    }

    @Override
    public String produce() {
        return this.verify(suffix);
    }
}
