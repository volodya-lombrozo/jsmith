package com.github.lombrozo.jsmith.model.nodes.statements;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.expressions.Expression;

//ifThenStatement
//        :	'if' '(' expression ')' statement
//        ;

public class IfThenStatement implements IStatement {

    private Expression expression;
    private IStatement statement;

    IfThenStatement(STKey key, ScopeTable scopeTable) {
        this.expression = new Expression(new STKey("boolean", scopeTable.isStaticScope()) , scopeTable);
        this.statement = new ExpressionStatement(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify("if (" + this.expression.produce() + ")" + this.statement.produce());
    }
}
