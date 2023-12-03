package com.github.lombrozo.jsmith.model.nodes.literals;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.nodes.Node;

import java.util.Random;

//DecimalNumeral
//        :	'0'
//        |	NonZeroDigit (Digits? | Underscores Digits)
//        ;

public class DecimalNumeral implements Node{

    private String digits;

    DecimalNumeral(STKey key) {

        Random rand = new Random();

        if (rand.nextInt(10) > 0) {
            switch (key.getType()) {
                case "long":
                    this.digits = String.valueOf(rand.nextLong());
                    break;
                case "byte":
                    this.digits = String.valueOf(Math.abs(rand.nextInt(8)));
                    break;
                case "char":
                    this.digits = String.valueOf(Math.abs(rand.nextInt(256)));
                    break;
                case "short":
                    this.digits = String.valueOf(rand.nextInt(Short.MAX_VALUE));
                    break;
                case "int":
                    this.digits = String.valueOf(rand.nextInt());
                    break;
            }

        } else {
            this.digits = "0";
        }
    }

    @Override
    public String produce() {
        return this.verify(digits);
    }
}
