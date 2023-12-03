package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.literals.IPrimaryNoNewArray_lfno_primary;

//methodInvocation_lfno_primary
//        :	methodName '(' argumentList? ')'
//        |	typeName '.' typeArguments? Identifier '(' argumentList? ')'
//        |	expressionName '.' typeArguments? Identifier '(' argumentList? ')'
//        |	'super' '.' typeArguments? Identifier '(' argumentList? ')'
//        |	typeName '.' 'super' '.' typeArguments? Identifier '(' argumentList? ')'
//        ;

public class MethodInvocation_lfno_primary implements IPrimaryNoNewArray_lfno_primary {

    private String methodName;

    MethodInvocation_lfno_primary(STKey key, ScopeTable scopeTable) {
        this.methodName = scopeTable.getRandomMethod(key).getIdentifier();
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();
        b.append(methodName);
        b.append('(');
        b.append(')');
        return this.verify(b.toString());
    }

    public String getMethodName() {
        return methodName;
    }
}
