package com.github.lombrozo.jsmith.model.nodes.statements;

//statementWithoutTrailingSubstatement
//        :	block
//        |	emptyStatement
//        |	expressionStatement
//        |	assertStatement
//        |	switchStatement
//        |	doStatement
//        |	breakStatement
//        |	continueStatement
//        |	returnStatement
//        |	synchronizedStatement
//        |	throwStatement
//        |	tryStatement
//        ;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;

public class StatementWithoutTrailingSubstatement implements IStatement {

    private IStatementWithoutTrailingSubstatement statementWithoutTrailingSubstatement;

    StatementWithoutTrailingSubstatement(ScopeTable scopeTable, STKey key) {
        this.statementWithoutTrailingSubstatement = new ReturnStatement(scopeTable, key);
    }

    @Override
    public String produce() {
        return this.verify(statementWithoutTrailingSubstatement.produce());
    }
}
