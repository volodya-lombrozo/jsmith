package com.github.lombrozo.jsmith.model.nodes.statements;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.ArrayList;
import java.util.List;

//blockStatements
//        :	blockStatement+
//        ;

public class BlockStatements implements Node {

    private List<IBlockStatement> blockStatementList;

    private static final int MIN_NUMBER_OF_STATEMENTS = 40;
    private static final int MAX_NUMBER_OF_STATEMENTS = 60;

    BlockStatements(STKey key, ScopeTable scopeTable) {
        this.blockStatementList = new ArrayList<>();

        for (int i = 0; i < RandomGen.getNextInt(MAX_NUMBER_OF_STATEMENTS - MIN_NUMBER_OF_STATEMENTS) + MIN_NUMBER_OF_STATEMENTS; i++) {
            this.blockStatementList.add(new BlockStatement(scopeTable, key));
        }

        if (!key.getType().equals("void")) {
            this.blockStatementList.add(new ReturnStatement(scopeTable, key));
        }
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();
        for (IBlockStatement blockStatement : this.blockStatementList) {
            b.append(blockStatement.produce());
        }

        return this.verify(b.toString());
    }
}
