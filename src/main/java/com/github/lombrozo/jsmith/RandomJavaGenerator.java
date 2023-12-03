package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.model.nodes.classes.NormalClassDeclaration;
import com.github.lombrozo.jsmith.model.nodes.interfaces.InterfaceDeclaration;
import com.github.lombrozo.jsmith.parser.Parser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomJavaGenerator {

    private static final Logger log = Logger.getLogger(RandomJavaGenerator.class.getName());

    private final Parser parser;

    private Map<String, ScopeTable> interfaceTables;

    private List<String> fileNameList;

    private List<String> interfacesSrcs;
    private List<String> classSrcs;

    private int numberOfInterfaces = 3;
    private int numberOfClasses = 5;

    public static String GENERATION_PATH = "generatedSrc";

    private boolean clearBasePath = false;

    public RandomJavaGenerator() {
        this.parser = new Parser();
        initFilePath();
    }

    public RandomJavaGenerator(String generationPath) {
        this.parser = new Parser();
        GENERATION_PATH = generationPath;
        initFilePath();
    }

    public RandomJavaGenerator(boolean clearBasePath) {
        this.parser = new Parser();
        this.clearBasePath = clearBasePath;
        initFilePath();
    }

    public RandomJavaGenerator(boolean clearBasePath, String generationPath) {
        this.parser = new Parser();
        this.clearBasePath = clearBasePath;
        GENERATION_PATH = generationPath;
        initFilePath();
    }

    public RandomJavaGenerator(int numberOfClasses, int numberOfInterfaces) {
        this.parser = new Parser();
        this.numberOfClasses = numberOfClasses;
        this.numberOfInterfaces = numberOfInterfaces;
        initFilePath();
    }

    public RandomJavaGenerator(int numberOfClasses, int numberOfInterfaces, String generationPath) {
        this.parser = new Parser();
        this.numberOfClasses = numberOfClasses;
        this.numberOfInterfaces = numberOfInterfaces;
        GENERATION_PATH = generationPath;
        initFilePath();
    }

    public RandomJavaGenerator(int numberOfClasses, int numberOfInterfaces, boolean clearBasePath) {
        this.clearBasePath = clearBasePath;
        this.parser = new Parser();
        this.numberOfClasses = numberOfClasses;
        this.numberOfInterfaces = numberOfInterfaces;
        initFilePath();
    }

    public RandomJavaGenerator(int numberOfClasses, int numberOfInterfaces, boolean clearBasePath, String generationPath) {
        this.clearBasePath = clearBasePath;
        this.parser = new Parser();
        this.numberOfClasses = numberOfClasses;
        this.numberOfInterfaces = numberOfInterfaces;
        GENERATION_PATH = generationPath;
        initFilePath();
    }

    public RandomJavaGenerator(int numberOfClasses) {
        if (numberOfClasses < 1) {
            throw new IllegalArgumentException("Number of classes should be greater than 1");
        }
        this.parser = new Parser();
        this.numberOfClasses = numberOfClasses;
        initFilePath();
    }

    public RandomJavaGenerator(int numberOfClasses, boolean clearBasePath) {
        if (numberOfClasses < 1) {
            throw new IllegalArgumentException("Number of classes should be greater than 1");
        }
        this.parser = new Parser();
        this.numberOfClasses = numberOfClasses;
        this.clearBasePath = clearBasePath;
        initFilePath();
    }

    public RandomJavaGenerator(int numberOfClasses, boolean clearBasePath, String generationPath) {
        if (numberOfClasses < 1) {
            throw new IllegalArgumentException("Number of classes should be greater than 1");
        }
        this.parser = new Parser();
        this.numberOfClasses = numberOfClasses;
        this.clearBasePath = clearBasePath;
        GENERATION_PATH = generationPath;
        initFilePath();
    }

    public boolean generate() {
        interfaceTables = new HashMap<>();
        fileNameList = new ArrayList<>();
        classSrcs = new ArrayList<>();
        interfacesSrcs = new ArrayList<>();
        try {
            generateInterfaces();
            generateClasses();
            compileFiles();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void generateInterfaces() {
        for (int i = 0; i < numberOfInterfaces; i++) {
            InterfaceDeclaration interfaceDeclaration = new InterfaceDeclaration();
            String interfaceName = interfaceDeclaration.getNormalInterfaceDeclaration().getIdentifier().produce();
            fileNameList.add(interfaceName + ".java");

            save(interfaceDeclaration, GENERATION_PATH + "/main/java/" + interfaceName + ".java");
            interfacesSrcs.add(interfaceDeclaration.produce());
            log.info("[INTERFACE GENERATOR] Generation successful: " + interfaceName);

            ScopeTable table = parser.getClassScopeTable(new File(GENERATION_PATH + "/main/java/" + interfaceName + ".java"));
            interfaceTables.put(interfaceName, table);
        }
    }

    private void generateClasses() {
        for (int i = 0; i < numberOfClasses; i++) {
            boolean produceMain = (i == (numberOfClasses - 1));
            NormalClassDeclaration classDeclaration = new NormalClassDeclaration(produceMain, interfaceTables);

            fileNameList.add(classDeclaration.getClassIdentifier() + ".java");
            save(classDeclaration, GENERATION_PATH + "/main/java/" + classDeclaration.getClassIdentifier() + ".java");
            classSrcs.add(classDeclaration.produce());

            log.info("CLASS GENERATOR: Generation successful: " + classDeclaration.getClassIdentifier());
        }
    }

    private void compileFiles() {
        if (CompileChecker.compileCheck(fileNameList) == 0) {
            log.info("[COMPILER]: Compilation successful");
            Runner runner = new Runner(true, 5000);
            runner.execute(fileNameList.get(fileNameList.size() - 1));
        } else {
            log.log(Level.SEVERE, "[Compiler] Compilation failed \n Exit from random program generator");
        }
    }

    private void save(Node node, String path) {
        List<String> sourceLines = new ArrayList<>();
        sourceLines.add(node.produce());

        File f = new File(path);
        try {
            Files.write(f.toPath(), sourceLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initFilePath() {
        new File(GENERATION_PATH + "/out").mkdirs();
        new File(GENERATION_PATH + "/main/java").mkdirs();
        if (clearBasePath) {
            try {
                FileUtils.cleanDirectory(new File(GENERATION_PATH + "/main/java"));
                FileUtils.cleanDirectory(new File(GENERATION_PATH + "/out"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getInterfacesSrcs() {
        return interfacesSrcs;
    }

    public List<String> getClassSrcs() {
        return classSrcs;
    }
}
