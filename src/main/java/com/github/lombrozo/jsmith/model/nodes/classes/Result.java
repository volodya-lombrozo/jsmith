package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.types.UnannType;
import com.github.lombrozo.jsmith.utils.RandomGen;

//result
//        :	unannType
//        |	'void'
//        ;

public class Result implements Node{

    private String type;

    Result() {
        if (RandomGen.getNextInt(2) == 1) {
            this.type = new UnannType().produce();
        } else {
            this.type = "void";
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public String produce() {
        return this.verify(type);
    }
}
