package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//assignmentExpression
//        :	conditionalExpression
//        |	assignment
//        ;

public class AssignmentExpression implements Node {

    private ConditionalExpression conditionalExpression;

    AssignmentExpression(STKey key, ScopeTable scopeTable) {
        this.conditionalExpression = new ConditionalExpression(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(conditionalExpression.produce());
    }
}
