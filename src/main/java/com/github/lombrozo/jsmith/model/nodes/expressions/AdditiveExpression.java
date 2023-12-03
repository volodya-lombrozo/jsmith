package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//additiveExpression
//        :	multiplicativeExpression
//        |	additiveExpression '+' multiplicativeExpression
//        |	additiveExpression '-' multiplicativeExpression
//        ;

public class AdditiveExpression implements Node
{

    private MultiplicativeExpression multiplicativeExpression;

    public AdditiveExpression(STKey key, ScopeTable scopeTable) {
        this.multiplicativeExpression = new MultiplicativeExpression(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(multiplicativeExpression.produce());
    }
}
