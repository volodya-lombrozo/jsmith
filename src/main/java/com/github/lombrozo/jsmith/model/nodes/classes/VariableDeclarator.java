package com.github.lombrozo.jsmith.model.nodes.classes;

//variableDeclarator
//        :	variableDeclaratorId ('=' variableInitializer)?
//        ;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

public class VariableDeclarator implements Node {

    private VariableDeclaratorId variableDeclaratorId;
    private VariableInitializer variableInitializer;

    VariableDeclarator(STKey key, ScopeTable scopeTable) {
        this.variableDeclaratorId = new VariableDeclaratorId();
        this.variableInitializer = new VariableInitializer(key, scopeTable);
    }

    public String getVariableDeclaratorId() {
        return variableDeclaratorId.getId();
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();

        b.append(this.variableDeclaratorId.produce());
        String initializer = this.variableInitializer.produce();
        if (initializer.length() > 0) {
            b.append("=").append(initializer);
        }

        return this.verify(b.toString());
    }

}
