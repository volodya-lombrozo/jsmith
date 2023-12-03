package com.github.lombrozo.jsmith.model.nodes.classes;

//variableDeclaratorList
//        :	variableDeclarator (',' variableDeclarator)*
//        ;

import com.github.lombrozo.jsmith.model.STEntry;
import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclaratorList implements Node {

    private List<VariableDeclarator> variableDeclaratorList;

    // Maximum length of variable declarator list
    private static int VARIABLE_DECLARATION_LIST_LENGTH = 3;

    public VariableDeclaratorList(STKey key, String varType, ScopeTable scopeTable) {
        this.variableDeclaratorList = new ArrayList<>();

        for (int i = 0; i < RandomGen.getNextInt(VARIABLE_DECLARATION_LIST_LENGTH) + 1; i++) {
            VariableDeclarator newVar = new VariableDeclarator(key, scopeTable);
            if (varType.equals("field")) {
                scopeTable.addField(
                        key.getType(),
                        new STEntry(key.getType(), newVar.getVariableDeclaratorId(), key.isStatic())
                );
            } else {
                scopeTable.addVariable(
                        key.getType(),
                        new STEntry(key.getType(), newVar.getVariableDeclaratorId(), key.isStatic())
                );
            }
            this.variableDeclaratorList.add(newVar);
        }
    }

    @Override
    public String produce() {
        StringBuilder b = new StringBuilder();
        b.append(this.variableDeclaratorList.get(0).produce());

        for (int i = 1; i < this.variableDeclaratorList.size(); i++) {
            b.append(",").append(this.variableDeclaratorList.get(i).produce());
        }

        return this.verify(b.toString());
    }
}
