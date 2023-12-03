package com.github.lombrozo.jsmith.model.nodes.statements;

//localVariableDeclaration
//        :	variableModifier* unannType variableDeclaratorList
//        ;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.classes.VariableDeclaratorList;
import com.github.lombrozo.jsmith.model.nodes.classes.VariableModifier;
import com.github.lombrozo.jsmith.model.nodes.types.UnannType;

public class LocalVariableDeclaration implements IBlockStatement{

    private VariableModifier variableModifier;
    private UnannType unannType;
    private VariableDeclaratorList variableDeclaratorList;

    LocalVariableDeclaration(ScopeTable scopeTable, STKey key) {
        this.variableModifier = new VariableModifier();
        this.unannType = new UnannType();
        this.variableDeclaratorList = new VariableDeclaratorList(new STKey(this.unannType.getType(), key.isStatic()), "var", scopeTable);
    }

    @Override
    public String produce() {
        String b = this.variableModifier.produce() +
                " " +
                this.unannType.produce() +
                " " +
                this.variableDeclaratorList.produce();
        return this.verify(b);
    }
}
