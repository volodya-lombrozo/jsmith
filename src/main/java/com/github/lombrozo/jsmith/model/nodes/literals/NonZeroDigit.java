package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RegexGen;

//        NonZeroDigit
//        :	[1-9]
//        ;

public class NonZeroDigit implements Node {

    private String nonZeroDigit;

    NonZeroDigit() {
        this.nonZeroDigit = (new RegexGen("[1-9]")).get();
    }

    @Override
    public String produce() {
        return this.verify(nonZeroDigit);
    }
}
