package com.github.lombrozo.jsmith.parser;

import com.github.lombrozo.jsmith.model.ScopeTable;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This class is used to initialize the parsing of the specified source code
 */
public class Parser {

    private ScopeTable classScopeTable;

    public Parser() {
        classScopeTable = new ScopeTable();
    }

    private void parse(File sourceCodePath, ScopeTable scopeTable) {
        try {
            String code = FileUtils.readFileToString(sourceCodePath);
            CompilationUnit compilationUnit = createCompilationUnit(code);
            parseWithVisitor(compilationUnit, scopeTable);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseWithVisitor(CompilationUnit compilationUnit, ScopeTable scopeTable) {
        MyVisitor myVisitor = new MyVisitor(compilationUnit, scopeTable);
        compilationUnit.accept(myVisitor);
        classScopeTable = myVisitor.getScopeTable();
    }

    private CompilationUnit createCompilationUnit(String code) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        parser.setUnitName(code);
        parser.setSource(code.toCharArray());
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        return (CompilationUnit) parser.createAST(null);
    }

    public ScopeTable getClassScopeTable(File file, ScopeTable scopeTable) {
        parse(file, scopeTable);
        return classScopeTable;
    }

    public ScopeTable getClassScopeTable(File file) {
        parse(file);
        return classScopeTable;
    }

    private void parse(File sourceCodePath) {
        try {
            String code = FileUtils.readFileToString(sourceCodePath);
            CompilationUnit compilationUnit = createCompilationUnit(code);
            parseWithVisitor(compilationUnit);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseWithVisitor(CompilationUnit compilationUnit) {
        MyVisitor myVisitor = new MyVisitor(compilationUnit);
        compilationUnit.accept(myVisitor);
        classScopeTable = myVisitor.getScopeTable();
    }
}
