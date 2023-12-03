package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;

//primary
//        :	(	primaryNoNewArray_lfno_primary
//        |	arrayCreationExpression
//        )
//        (	primaryNoNewArray_lf_primary
//        )*
//        ;

public class Primary implements IPostfixExpression{

    private PrimaryNoNewArray_lfno_primary primaryNoNewArray_lfno_primary;

    Primary(STKey key, ScopeTable scopeTable) {
        this.primaryNoNewArray_lfno_primary = new PrimaryNoNewArray_lfno_primary(key, scopeTable);
    }

    @Override
    public String produce() {
        return this.verify(primaryNoNewArray_lfno_primary.produce());
    }
}
