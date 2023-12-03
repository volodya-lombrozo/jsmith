package com.github.lombrozo.jsmith.model.nodes.statements;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;

public class MethodInvocation implements IStatementExpression {

    private String methodName;

    MethodInvocation(ScopeTable scopeTable) {
        this.methodName = scopeTable.getRandomMethod(new STKey(null, scopeTable.isStaticScope())).getIdentifier();
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String produce() {
        return this.verify(methodName + "()");
    }
}
