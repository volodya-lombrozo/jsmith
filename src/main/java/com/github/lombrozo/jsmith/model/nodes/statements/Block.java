package com.github.lombrozo.jsmith.model.nodes.statements;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

//block
//        :	'{' blockStatements? '}'
//        ;

public class Block implements Node{

    private BlockStatements blockStatements;

    public Block(STKey key, ScopeTable outerScopeTable) {
        ScopeTable scopeTable = new ScopeTable(outerScopeTable, outerScopeTable.isStaticScope());
        this.blockStatements = new BlockStatements(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify("{" + blockStatements.produce() + "}");
    }
}
