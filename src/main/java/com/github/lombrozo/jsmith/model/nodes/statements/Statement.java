package com.github.lombrozo.jsmith.model.nodes.statements;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;

//statement
//        :	statementWithoutTrailingSubstatement
//        |	labeledStatement
//        |	ifThenStatement
//        |	ifThenElseStatement
//        |	whileStatement
//        |	forStatement
//        ;

public class Statement implements IBlockStatement{

    private IStatement statement;

    Statement(STKey key, ScopeTable scopeTable) {
//        this.statement = new StatementWithoutTrailingSubstatement(scopeTable, key);
        this.statement = new IfThenStatement(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(statement.produce());
    }
}
