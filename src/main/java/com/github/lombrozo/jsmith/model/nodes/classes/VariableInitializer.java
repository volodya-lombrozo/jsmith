package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.expressions.AdditiveExpression;

//variableInitializer
//        :	expression
//        |	arrayInitializer
//        ;

public class VariableInitializer implements Node {

    private AdditiveExpression expression;

    VariableInitializer(STKey key, ScopeTable scopeTable) {
        this.expression = new AdditiveExpression(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(this.expression.produce());
    }
}
