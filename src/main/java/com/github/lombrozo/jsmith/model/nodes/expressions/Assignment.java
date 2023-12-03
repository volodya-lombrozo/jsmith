package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.statements.IStatementExpression;

//assignment
//        :	leftHandSide assignmentOperator expression
//        ;


public class Assignment implements IStatementExpression {

    private LeftHandSide leftHandSide;
    private AdditiveExpression expression;

    public Assignment(ScopeTable scopeTable) {
        this.leftHandSide = new LeftHandSide(scopeTable);
        this.expression = new AdditiveExpression(
                new STKey(
                        this.leftHandSide.getExpressionName().getExpressionName().getType(),
                        scopeTable.isStaticScope()
                ),
                scopeTable
        );
    }

    @Override
    public String produce() {
        return this.verify(this.leftHandSide.produce() + "=" + this.expression.produce());
    }
}
