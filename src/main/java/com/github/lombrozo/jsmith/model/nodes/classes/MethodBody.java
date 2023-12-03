package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.statements.Block;

//methodBody
//        :	block
//        |	';'
//        ;

public class MethodBody implements Node {

    private Block block;

    MethodBody(STKey key, ScopeTable scopeTable) {
        this.block = new Block(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(block.produce());
    }
}
