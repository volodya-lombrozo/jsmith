package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RegexGen;

//        Digits
//        :	Digit (DigitsAndUnderscores? Digit)?
//        ;

//        Digit
//        :	'0'
//        |	NonZeroDigit
//        ;

public class Digits implements Node {

    private String digits;

    Digits() {
        this.digits = (new RegexGen("[0-9]+")).get();
    }

    @Override
    public String produce() {
        return this.verify(digits);
    }

}
