package com.github.lombrozo.jsmith.model.nodes.classes;

import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.Map;

public class SuperInterfaces implements Node {

    private String interfaceName;

    SuperInterfaces(Map<String, ScopeTable> interfaceTable) {
        this.interfaceName = (String) interfaceTable.keySet().toArray()[RandomGen.getNextInt(interfaceTable.keySet().size())];
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    @Override
    public String produce() {
        return this.verify("implements " + this.interfaceName);
    }
}
