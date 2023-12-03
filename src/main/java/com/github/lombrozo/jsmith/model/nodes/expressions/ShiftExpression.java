package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//shiftExpression
//        :	additiveExpression
//        |	shiftExpression '<' '<' additiveExpression
//        |	shiftExpression '>' '>' additiveExpression
//        |	shiftExpression '>' '>' '>' additiveExpression
//        ;

public class ShiftExpression implements Node {

    private AdditiveExpression additiveExpression;

    ShiftExpression(STKey key, ScopeTable scopeTable) {
        this.additiveExpression = new AdditiveExpression(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(additiveExpression.produce());
    }
}
