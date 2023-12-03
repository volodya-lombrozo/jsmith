package com.github.lombrozo.jsmith.utils;

import java.util.Map;

public class Config {

    private Map<String, String> identifier;
    private Map<String, Integer> fields;
    private Map<String, Integer> methods;
    private Map<String, Integer> statements;
    private Map<String, Integer> classes;
    private Map<String, Integer> interfaces;
    private int variableDeclaratorListLength;
    private boolean run;
    private int timeout;
    private String generationPath;
    private boolean clearBasePath;

    public Map<String, String> getIdentifier() {
        return identifier;
    }

    public Map<String, Integer> getFields() {
        return fields;
    }

    public Map<String, Integer> getMethods() {
        return methods;
    }

    public Map<String, Integer> getStatements() {
        return statements;
    }

    public Map<String, Integer> getClasses() {
        return classes;
    }

    public Map<String, Integer> getInterfaces() {
        return interfaces;
    }

    public int getVariableDeclaratorListLength() {
        return variableDeclaratorListLength;
    }

    public boolean isRun() {
        return run;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getGenerationPath() {
        return generationPath;
    }

    public boolean isClearBasePath() {
        return clearBasePath;
    }
}
