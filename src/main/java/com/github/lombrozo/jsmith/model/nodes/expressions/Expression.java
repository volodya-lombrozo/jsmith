package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//expression
//        :	lambdaExpression
//        |	assignmentExpression
//        ;

public class Expression implements Node {

    private AssignmentExpression assignmentExpression;

    public Expression(STKey key, ScopeTable scopeTable) {
        this.assignmentExpression = new AssignmentExpression(key, scopeTable);
    }

    public String produce() {
        return this.verify(this.assignmentExpression.produce());
    }
}
