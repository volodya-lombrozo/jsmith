package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

//conditionalAndExpression
//        :	inclusiveOrExpression
//        |	conditionalAndExpression '&&' inclusiveOrExpression
//        ;

public class ConditionalAndExpression implements Node {

    private InclusiveOrExpression inclusiveOrExpression;
    private ConditionalAndExpression conditionalAndExpression;

    private boolean unary;

    ConditionalAndExpression(STKey key, ScopeTable scopeTable) {
        this.unary = (RandomGen.getNextInt(10) != 0);

        this.inclusiveOrExpression = new InclusiveOrExpression(key, scopeTable);

        if (!this.unary) {
            this.conditionalAndExpression = new ConditionalAndExpression(key, scopeTable);
        }

    }

    @Override
    public String produce() {
        if (this.unary) {
            return this.verify(inclusiveOrExpression.produce());
        } else {
            String prod = this.conditionalAndExpression.produce() + "&&" + this.inclusiveOrExpression.produce();
            return this.verify(prod);
        }

    }
}
