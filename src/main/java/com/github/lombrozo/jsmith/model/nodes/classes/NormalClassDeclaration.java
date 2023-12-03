package com.github.lombrozo.jsmith.model.nodes.classes;

//normalClassDeclaration
//        :	classModifier* 'class' Identifier typeParameters? superclass? superinterfaces? classBody
//        ;

import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.identifiers.ClassIdentifier;

import java.util.Map;

public class NormalClassDeclaration implements Node {

    private ClassIdentifier identifier;
    private ClassBody classBody;
    private ScopeTable scopeTable;
    private SuperInterfaces superInterfaces;
    private boolean implementsInterface = false;
    private ScopeTable overriddenMethods = null;

    public NormalClassDeclaration(boolean produceMain, Map<String, ScopeTable> interfaceTables) {
        if (!interfaceTables.isEmpty()) {
            this.implementsInterface = true;
            this.superInterfaces = new SuperInterfaces(interfaceTables);
            this.overriddenMethods = interfaceTables.get(this.superInterfaces.getInterfaceName());
        }

        this.identifier = new ClassIdentifier("Class");
        this.scopeTable = new ScopeTable();
        this.classBody = new ClassBody(scopeTable, produceMain, implementsInterface, this.overriddenMethods);
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();
        b.append("class ").append(this.identifier.produce());
        if (implementsInterface) {
            b.append(" ").append(this.superInterfaces.produce());
        }
        b.append(classBody.produce());

        return this.verify(b.toString());
    }

    public String getClassIdentifier() {
        return this.identifier.getIdentifier();
    }
}
