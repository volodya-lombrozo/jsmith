package com.github.lombrozo.jsmith.model.nodes.statements;

//localVariableDeclarationStatement
//        :	localVariableDeclaration ';'
//        ;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;

public class LocalVariableDeclarationStatement implements IBlockStatement {

    private LocalVariableDeclaration localVariableDeclaration;

    LocalVariableDeclarationStatement(ScopeTable scopeTable, STKey key) {
        this.localVariableDeclaration = new LocalVariableDeclaration(scopeTable, key);
    }

    @Override
    public String produce() {
        return this.verify(localVariableDeclaration.produce() + ";");
    }
}
