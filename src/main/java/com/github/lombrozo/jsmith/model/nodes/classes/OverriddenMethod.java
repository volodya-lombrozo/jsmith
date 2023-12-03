package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.STEntry;
import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;

public class OverriddenMethod implements Node {

    private MethodBody methodBody;
    private STEntry params;

    OverriddenMethod(STEntry params, ScopeTable scopeTable) {
        this.params = params;
        this.methodBody = new MethodBody(new STKey(params.getType(), false), scopeTable);
    }

    @Override
    public String produce() {
        return this.verify("@Override public " + this.params.getType() + " " + this.params.getIdentifier() + "() " + this.methodBody.produce());
    }
}
