package com.github.lombrozo.jsmith.model.nodes.classes;

//methodDeclaration
//        :	methodModifier* methodHeader methodBody
//        ;

import com.github.lombrozo.jsmith.model.STEntry;
import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;

public class MethodDeclaration implements IClassMemberDeclaration {

    private MethodModifier methodModifier;
    private MethodHeader methodHeader;
    private MethodBody methodBody;

    MethodDeclaration(ScopeTable outerScopeTable) {
        this.methodModifier = new MethodModifier();
        this.methodHeader = new MethodHeader();

        ScopeTable scopeTable = new ScopeTable(outerScopeTable, this.methodModifier.isStatic());

        this.methodBody = new MethodBody(new STKey(this.methodHeader.getResult().getType(), this.methodModifier.isStatic()), scopeTable);

        outerScopeTable.addMethod(
                this.methodHeader.getResult().getType(),
                new STEntry(
                        this.methodHeader.getResult().getType(),
                        this.methodHeader.getMethodDeclarator().getIdentifier().toString(),
                        this.methodModifier.isStatic()
                )
        );
    }

    @Override
    public String produce() {
        String b = methodModifier.produce() +
                " " +
                methodHeader.produce() +
                " " +
                methodBody.produce();
        return this.verify(b);
    }
}
