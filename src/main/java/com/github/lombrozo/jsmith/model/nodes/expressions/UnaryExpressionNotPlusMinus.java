package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//unaryExpressionNotPlusMinus
//        :	postfixExpression
//        |	'~' unaryExpression
//        |	'!' unaryExpression
//        |	castExpression
//        ;

public class UnaryExpressionNotPlusMinus implements Node{

    private PostfixExpression postfixExpression;

    public UnaryExpressionNotPlusMinus(STKey key, ScopeTable scopeTable) {
        this.postfixExpression = new PostfixExpression(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(postfixExpression.produce());
    }
}
