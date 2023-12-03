package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STEntry;
import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.utils.RandomGen;

//expressionName
//        :	Identifier
//        |	ambiguousName '.' Identifier
//        ;

public class ExpressionName implements IPostfixExpression {

    private STEntry expressionName;

    ExpressionName(ScopeTable scopeTable) {
        if (RandomGen.getNextInt(2) == 1) {
            this.expressionName = scopeTable.getRandomVariable(new STKey(null, scopeTable.isStaticScope()));
            if (this.expressionName.getIdentifier() == null) {
                this.expressionName = scopeTable.getRandomField(new STKey(null, scopeTable.isStaticScope()));
            }
        } else {
            this.expressionName = scopeTable.getRandomField(new STKey(null, scopeTable.isStaticScope()));
            if (this.expressionName.getIdentifier() == null) {
                this.expressionName = scopeTable.getRandomVariable(new STKey(null, scopeTable.isStaticScope()));
            }
        }
    }

    ExpressionName(STKey key, ScopeTable scopeTable) {
        if (RandomGen.getNextInt(2) == 1) {
            this.expressionName = scopeTable.getRandomVariable(key);
            if (this.expressionName.getIdentifier() == null) {
                this.expressionName = scopeTable.getRandomField(key);
            }
        } else {
            this.expressionName = scopeTable.getRandomField(key);
            if (this.expressionName.getIdentifier() == null) {
                this.expressionName = scopeTable.getRandomVariable(key);
            }
        }
    }

    public STEntry getExpressionName() {
        return expressionName;
    }

    @Override
    public String produce() {
        return this.verify(expressionName.getIdentifier());
    }
}
