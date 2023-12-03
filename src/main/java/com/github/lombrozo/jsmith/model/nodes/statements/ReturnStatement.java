package com.github.lombrozo.jsmith.model.nodes.statements;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.expressions.AdditiveExpression;

//returnStatement
//        :	'return' expression? ';'
//        ;

public class ReturnStatement implements IStatementWithoutTrailingSubstatement {

    private String returnStatement;

    ReturnStatement(ScopeTable scopeTable, STKey key) {
        this.returnStatement = new AdditiveExpression(key, scopeTable).produce();
    }

    @Override
    public String produce() {
        return this.verify("return " + returnStatement + ";");
    }
}
