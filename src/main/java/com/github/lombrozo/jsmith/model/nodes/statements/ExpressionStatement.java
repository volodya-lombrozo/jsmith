package com.github.lombrozo.jsmith.model.nodes.statements;

//expressionStatement
//        :	statementExpression ';'
//        ;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;

public class ExpressionStatement implements IStatement {

    private StatementExpression statementExpression;

    ExpressionStatement(STKey key, ScopeTable scopeTable) {
        this.statementExpression = new StatementExpression(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(this.statementExpression.produce() + ";");
    }
}
