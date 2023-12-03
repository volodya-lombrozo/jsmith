package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

//andExpression
//        :	equalityExpression
//        |	andExpression '&' equalityExpression
//        ;

public class AndExpression implements Node {

    private EqualityExpression equalityExpression;
    private AndExpression andExpression;

    private boolean unary;

    AndExpression(STKey key, ScopeTable scopeTable) {
        this.unary = (RandomGen.getNextInt(10) != 0);

        this.equalityExpression = new EqualityExpression(key, scopeTable);

        if (!this.unary) {
            this.andExpression = new AndExpression(key, scopeTable);
        }
    }

    @Override
    public String produce() {
        if (this.unary) {
            return this.verify(equalityExpression.produce());
        } else {
            String prod = this.andExpression.produce() + "&" + this.equalityExpression.produce();
            return this.verify(prod);
        }

    }
}
