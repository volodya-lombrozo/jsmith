package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

//conditionalExpression
//        :	conditionalOrExpression
//        |	conditionalOrExpression '?' expression ':' conditionalExpression
//        ;

public class ConditionalExpression implements Node {

    private ConditionalOrExpression conditionalOrExpression;
    private Expression expression;
    private ConditionalExpression conditionalExpression;

    private boolean unary;

    ConditionalExpression(STKey key, ScopeTable scopeTable) {
        this.unary = (RandomGen.getNextInt(10) != 0);

        this.conditionalOrExpression = new ConditionalOrExpression(key, scopeTable);

        if (!this.unary) {
            this.expression = new Expression(key, scopeTable);
            this.conditionalExpression = new ConditionalExpression(key, scopeTable);
        }
    }

    @Override
    public String produce() {
        if (this.unary) {
            return this.verify(conditionalOrExpression.produce());
        } else {
            String prod = this.conditionalOrExpression.produce() + " ? " + this.expression.produce() + " : " + this.conditionalExpression.produce();
            return this.verify(prod);
        }
    }
}
