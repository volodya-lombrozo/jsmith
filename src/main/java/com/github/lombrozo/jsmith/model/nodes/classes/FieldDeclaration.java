package com.github.lombrozo.jsmith.model.nodes.classes;

//fieldDeclaration
//        :	fieldModifier* unannType variableDeclaratorList ';'
//        ;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.types.UnannType;

public class FieldDeclaration implements IClassMemberDeclaration {

    private FieldModifier fieldModifier;
    private UnannType unannType;
    private VariableDeclaratorList variableDeclaratorList;

    FieldDeclaration(ScopeTable scopeTable, String memberType) {
        this.fieldModifier = new FieldModifier();
        this.unannType = new UnannType();
        this.variableDeclaratorList = new VariableDeclaratorList(new STKey(this.unannType.getType(), this.fieldModifier.isStatic()), memberType, scopeTable);
    }

    @Override
    public String produce() {
        String b = this.fieldModifier.produce() +
                " " +
                this.unannType.produce() +
                " " +
                this.variableDeclaratorList.produce() +
                ";\n";
        return this.verify(b);
    }
}
