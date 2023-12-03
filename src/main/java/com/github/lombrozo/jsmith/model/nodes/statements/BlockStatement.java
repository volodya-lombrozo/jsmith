package com.github.lombrozo.jsmith.model.nodes.statements;

//blockStatement
//        :	localVariableDeclarationStatement
//        |	classDeclaration
//        |	statement
//        ;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.utils.RandomGen;

public class BlockStatement implements IBlockStatement{

    private IBlockStatement statement;

    BlockStatement(ScopeTable scopeTable, STKey key) {
        if (RandomGen.getNextInt(2) == 1) {
            if (scopeTable.isStaticScope() &&
                    (scopeTable.getRandomField(new STKey(null, true)).getIdentifier() == null ||
                            scopeTable.getRandomMethod(new STKey(null, true)).getIdentifier() == null)) {
                this.statement = new LocalVariableDeclarationStatement(scopeTable, key);
            } else {
                this.statement = new Statement(key, scopeTable);
            }
        } else {
            this.statement = new LocalVariableDeclarationStatement(scopeTable, key);
        }
    }

    @Override
    public String produce() {
        return this.verify(statement.produce());
    }
}
