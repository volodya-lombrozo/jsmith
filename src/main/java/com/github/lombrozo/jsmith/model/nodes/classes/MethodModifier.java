package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

//methodModifier
//        :	annotation
//        |	'public'
//        |	'protected'
//        |	'private'
//        |	'abstract'
//        |	'static'
//        |	'final'
//        |	'synchronized'
//        |	'native'
//        |	'strictfp'
//        ;

public class MethodModifier implements Node{

    private Visibility visibility;
    private boolean _static;

    MethodModifier() {
        this.visibility = Visibility.getRandomVisibility();
        this._static = RandomGen.getNextInt(4) == 0;
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();
        b.append(this.visibility.toString().toLowerCase());
        if (this.isStatic()) b.append(" static");
        return this.verify(b.toString());
    }

    public boolean isStatic() {
        return _static;
    }
}
