package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

//conditionalOrExpression
//        :	conditionalAndExpression
//        |	conditionalOrExpression '||' conditionalAndExpression
//        ;

public class ConditionalOrExpression implements Node {

    private ConditionalAndExpression conditionalAndExpression;
    private ConditionalOrExpression conditionalOrExpression;

    private boolean unary;

    ConditionalOrExpression(STKey key, ScopeTable scopeTable) {
        this.unary = (RandomGen.getNextInt(10) != 0);

        this.conditionalAndExpression = new ConditionalAndExpression(key, scopeTable);

        if (!this.unary) {
            this.conditionalOrExpression = new ConditionalOrExpression(key, scopeTable);
        }
    }

    @Override
    public String produce() {
        if (this.unary) {
            return this.verify(this.conditionalAndExpression.produce());
        } else {
            String prod = this.conditionalOrExpression.produce() + "||" + this.conditionalAndExpression.produce();
            return this.verify(prod);
        }
    }
}
