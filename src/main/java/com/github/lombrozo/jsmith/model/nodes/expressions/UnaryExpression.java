package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//unaryExpression
//        :	preIncrementExpression
//        |	preDecrementExpression
//        |	'+' unaryExpression
//        |	'-' unaryExpression
//        |	unaryExpressionNotPlusMinus
//        ;

public class UnaryExpression implements Node{

    private UnaryExpressionNotPlusMinus unaryExpressionNotPlusMinus;

    UnaryExpression(STKey key, ScopeTable scopeTable) {
        this.unaryExpressionNotPlusMinus = new UnaryExpressionNotPlusMinus(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(unaryExpressionNotPlusMinus.produce());
    }
}
