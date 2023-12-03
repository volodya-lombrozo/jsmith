package com.github.lombrozo.jsmith.model.nodes.classes;

//classBody
//        :	'{' classBodyDeclaration* '}'
//        ;

import com.github.lombrozo.jsmith.model.STEntry;
import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.ArrayList;
import java.util.List;

public class ClassBody implements Node {

    private final boolean implementsInterface;
    private final ScopeTable overriddenMethods;
    private boolean produceMain;

    private String mainMethod;
    private List<ClassBodyDeclaration> classBodyDeclarations;
    private List<OverriddenMethod> overriddenMethodList;

    private static final int MIN_NUMBER_OF_FIELDS = 4;
    private static final int MAX_NUMBER_OF_FIELDS = 6;

    private static final int MIN_NUMBER_OF_METHODS = 5;
    private static final int MAX_NUMBER_OF_METHODS = 9;

    ClassBody(ScopeTable scopeTable, boolean produceMain, boolean implementsInterface, ScopeTable overriddenMethods) {
        this.implementsInterface = implementsInterface;
        this.overriddenMethods = overriddenMethods;
        this.classBodyDeclarations = new ArrayList<>();
        this.produceMain = produceMain;
        init(scopeTable);

        // new STKey is related to the main method in order to get only static variable from the scopetable
        MethodBody mainMethodBody = new MethodBody(new STKey("void", true), new ScopeTable(scopeTable, true));

        if (this.produceMain) {
            this.mainMethod = "public static void main(String[] args)" +
                    mainMethodBody.produce();
        }

    }

    private void init(ScopeTable scopeTable) {

        // GENERATE FIELDS
        for (int i = 0; i < RandomGen.getNextInt(MAX_NUMBER_OF_FIELDS - MIN_NUMBER_OF_FIELDS) + MIN_NUMBER_OF_FIELDS; i++) {
            this.classBodyDeclarations.add(new ClassBodyDeclaration("field", scopeTable));
        }

        if (this.implementsInterface) {
            this.overriddenMethodList = new ArrayList<>();
            for (STEntry method : this.overriddenMethods.getMethods()) {
                this.overriddenMethodList.add(new OverriddenMethod(method, scopeTable));
            }
        }

        // GENERATE METHODS
        for (int i = 0; i < RandomGen.getNextInt(MAX_NUMBER_OF_METHODS - MIN_NUMBER_OF_METHODS) + MIN_NUMBER_OF_METHODS; i++) {
            this.classBodyDeclarations.add(new ClassBodyDeclaration("method", scopeTable));
        }
    }

    @Override
    public String produce() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (ClassBodyDeclaration dec : this.classBodyDeclarations) {
            builder.append(dec.produce());
        }
        if (this.implementsInterface) {
            for (OverriddenMethod method : overriddenMethodList) {
                builder.append(method.produce());
            }
        }
        if (this.produceMain) {
            builder.append(this.mainMethod);
        }
        builder.append("}");

        return this.verify(builder.toString());
    }
}
