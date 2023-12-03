package com.github.lombrozo.jsmith.model.nodes.expressions;

//leftHandSide
//	:	expressionName
//	|	fieldAccess
//	|	arrayAccess
//	;

import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

public class LeftHandSide implements Node {

    private ExpressionName expressionName;

    LeftHandSide(ScopeTable scopeTable) {
        this.expressionName = new ExpressionName(scopeTable);
    }

    public ExpressionName getExpressionName() {
        return expressionName;
    }

    @Override
    public String produce() {
        return this.verify(this.expressionName.produce());
    }
}
