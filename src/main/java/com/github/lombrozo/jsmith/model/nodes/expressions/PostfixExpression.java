package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

//postfixExpression
//        :	(	primary
//        |	expressionName
//        )
//        (	postIncrementExpression_lf_postfixExpression
//        |	postDecrementExpression_lf_postfixExpression
//        )*
//        ;

public class PostfixExpression implements Node{

    private IPostfixExpression postfixExpression;

    PostfixExpression(STKey key, ScopeTable scopeTable) {
        if (RandomGen.getNextInt(10) == 0) {
            this.postfixExpression = new Primary(key, scopeTable);
        } else {
            this.postfixExpression = new ExpressionName(key, scopeTable);
            if (this.postfixExpression.produce() == null) {
                this.postfixExpression = new Primary(key, scopeTable);
            }
        }


    }

    @Override
    public String produce() {
        return this.verify(postfixExpression.produce());
    }

}
