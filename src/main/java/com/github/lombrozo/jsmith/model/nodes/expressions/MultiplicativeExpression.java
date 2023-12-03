package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//multiplicativeExpression
//        :	unaryExpression
//        |	multiplicativeExpression '*' unaryExpression
//        |	multiplicativeExpression '/' unaryExpression
//        |	multiplicativeExpression '%' unaryExpression
//        ;

public class MultiplicativeExpression implements Node{

    private UnaryExpression unaryExpression;

    MultiplicativeExpression(STKey key, ScopeTable scopeTable) {
        this.unaryExpression = new UnaryExpression(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(unaryExpression.produce());
    }
}
