package com.github.lombrozo.jsmith.model;

import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.*;

/**
 * This class is the ScopeTable of the program. It contains three main parameters from which values are used to generate the source code.
 */
public class ScopeTable {

    private Map<String, ArrayList<STEntry>> fields;
    private Map<String, ArrayList<STEntry>> localVariables;
    private Map<String, ArrayList<STEntry>> methods;
    private boolean staticScope;

    public ScopeTable() {
        this.localVariables = new HashMap<>();
        this.methods = new HashMap<>();
        this.fields = new HashMap<>();
        this.staticScope = false;
    }

    public ScopeTable(ScopeTable outerScopeTable, boolean staticScope) {
        this.localVariables = deepCopy(outerScopeTable.localVariables);
        this.methods = deepCopy(outerScopeTable.methods);
        this.fields = deepCopy(outerScopeTable.fields);
        this.staticScope = staticScope;
    }

    public void addField(String type, STEntry identifier) {
        this.fields.computeIfAbsent(type, k -> new ArrayList<>()).add(identifier);
    }

    public void addVariable(String type, STEntry identifier) {
        this.localVariables.computeIfAbsent(type, k -> new ArrayList<>()).add(identifier);
    }

    public void addMethod(String type, STEntry identifier) {
        this.methods.computeIfAbsent(type, k -> new ArrayList<>()).add(identifier);
    }

    protected List<STEntry> getLocalVariables(String type) {
        return this.localVariables.get(type);
    }

    public List<STEntry> getMethods() {
        List<STEntry> flatMethods = new ArrayList<>();
        methods.values().stream().forEach(x -> x.stream().forEach(y -> flatMethods.add(y)));
        return flatMethods;
    }

    private STEntry getRandomType(Map<String, ArrayList<STEntry>> memberType, STKey key) {
        try {
            Object[] sel = memberType.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .filter(m -> m.isStatic() == key.isStatic())
                    .toArray();
            return (STEntry) sel[RandomGen.getNextInt(sel.length)];
        } catch (IllegalArgumentException | NullPointerException e) {
            return new STEntry(null, null, false);
        }
    }

    public STEntry getRandomVariable(STKey key) {
        if (key.getType() == null) return getRandomType(localVariables, key);

        try {
            List<STEntry> variables = this.localVariables.get(key.getType());
            return variables.get(RandomGen.getNextInt(variables.size()));
        } catch (NullPointerException e) {
            return new STEntry(null, null, false);
        }
    }

    public STEntry getRandomMethod(STKey key) {
        if (key.getType() == null) return getRandomType(methods, key);

        try {
            List<STEntry> methods = this.methods.get(key.getType());
            if (key.isStatic()) {
                return (STEntry) methods.stream().filter(v -> v.isStatic() == key.isStatic()).toArray()[RandomGen.getNextInt(methods.size())];
            } else {
                return methods.get(RandomGen.getNextInt(methods.size()));
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return new STEntry(null, null, false);
        }
    }

    public STEntry getRandomField(STKey key) {
        if (key.getType() == null) return getRandomType(fields, key);

        try {
            List<STEntry> fields = this.fields.get(key.getType());
            if (key.isStatic()) {
                return (STEntry) fields.stream().filter(v -> v.isStatic() == key.isStatic()).toArray()[RandomGen.getNextInt(fields.size())];
            } else {
                return fields.get(RandomGen.getNextInt(fields.size()));
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return new STEntry(null, null, false);
        }
    }

    private Map<String, ArrayList<STEntry>> deepCopy(Map<String, ArrayList<STEntry>> original) {

        Map<String, ArrayList<STEntry>> copy = new HashMap<>();
        for (Map.Entry<String, ArrayList<STEntry>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    public boolean isStaticScope() {
        return staticScope;
    }

    @Override
    public String toString() {

        return "Fields: " + fields.toString() + " \n " + "Methods: " + methods.toString() + " \n" + "Variables: " + localVariables.toString();
    }


}
