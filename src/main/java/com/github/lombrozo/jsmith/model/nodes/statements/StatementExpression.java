package com.github.lombrozo.jsmith.model.nodes.statements;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.expressions.Assignment;
import com.github.lombrozo.jsmith.utils.RandomGen;

//statementExpression
//        :	assignment
//        |	preIncrementExpression
//        |	preDecrementExpression
//        |	postIncrementExpression
//        |	postDecrementExpression
//        |	methodInvocation
//        |	classInstanceCreationExpression
//        ;

public class StatementExpression implements IStatement{

    private IStatementExpression statementExpression;

    StatementExpression(STKey key, ScopeTable scopeTable) {
        if (RandomGen.getNextInt(2) == 0) {
            this.statementExpression = new MethodInvocation(scopeTable);
            if (((MethodInvocation) this.statementExpression).getMethodName() == null) {
                this.statementExpression = new Assignment(scopeTable);
            }
        } else {
            try {
                this.statementExpression = new Assignment(scopeTable);
            } catch (NullPointerException e) {
                this.statementExpression = new MethodInvocation(scopeTable);
            }
        }
    }

    @Override
    public String produce() {
        return this.verify(statementExpression.produce());
    }
}
