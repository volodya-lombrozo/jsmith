package com.github.lombrozo.jsmith.utils;

import com.mifmif.common.regex.Generex;

public class RegexGen {

    private Generex generex;

    public RegexGen(String regex) {
        generex = new Generex(regex);
    }

    public String get() {
        return generex.random();
    }
}
