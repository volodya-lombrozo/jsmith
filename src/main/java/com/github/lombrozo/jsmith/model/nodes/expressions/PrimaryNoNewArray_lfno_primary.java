package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.literals.IPrimaryNoNewArray_lfno_primary;
import com.github.lombrozo.jsmith.model.nodes.literals.Literal;
import com.github.lombrozo.jsmith.utils.RandomGen;

//primaryNoNewArray_lfno_primary
//        :	literal
//        |	typeName ('[' ']')* '.' 'class'
//        |	unannPrimitiveType ('[' ']')* '.' 'class'
//        |	'void' '.' 'class'
//        |	'this'
//        |	typeName '.' 'this'
//        |	'(' expression ')'
//        |	classInstanceCreationExpression_lfno_primary
//        |	fieldAccess_lfno_primary
//        |	arrayAccess_lfno_primary
//        |	methodInvocation_lfno_primary
//        |	methodReference_lfno_primary
//        ;

public class PrimaryNoNewArray_lfno_primary implements Node{

    private IPrimaryNoNewArray_lfno_primary primaryNoNewArray_lfno_primary;

    PrimaryNoNewArray_lfno_primary(STKey key, ScopeTable scopeTable) {
        if (RandomGen.getNextInt(2) == 1) {
            this.primaryNoNewArray_lfno_primary = new Literal(key);
        } else {
            this.primaryNoNewArray_lfno_primary = new MethodInvocation_lfno_primary(key, scopeTable);
            if (((MethodInvocation_lfno_primary) this.primaryNoNewArray_lfno_primary).getMethodName() == null) {
                this.primaryNoNewArray_lfno_primary = new Literal(key);
            }
        }
    }

    @Override
    public String produce() {
        return this.verify(primaryNoNewArray_lfno_primary.produce());
    }
}
