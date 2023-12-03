package com.github.lombrozo.jsmith;

import com.github.lombrozo.jsmith.RandomJavaGenerator;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to compile and move the compiled source code in the out directory.
 */
public class CompileChecker {

    public static int compileCheck(String fileName) {

        File sourceDir = new File("./" + RandomJavaGenerator.GENERATION_PATH + "/main/java");
        File sourceFile = new File(sourceDir, fileName);

        // Compile source file.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // return 0 for success; nonzero otherwise
        return compiler.run(null, null, null, sourceFile.getPath());

    }

    /**
     * This method compiles all the file contained in fileNames
     *
     * @param fileNames the file list to compile
     * @return 0 if the compilation is successful
     */
    public static int compileCheck(List<String> fileNames) {

        List<String> toCompile = new ArrayList<>();
        for (String fileName : fileNames) {
            File sourceDir = new File("./" + RandomJavaGenerator.GENERATION_PATH + "/main/java");
            File sourceFile = new File(sourceDir, fileName);
            toCompile.add(sourceFile.getPath());
        }

        String[] stockArr = new String[toCompile.size()];
        stockArr = toCompile.toArray(stockArr);
        // Compile source file.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        String outputDir = RandomJavaGenerator.GENERATION_PATH + "/out";
        int result = compiler.run(null, null, null, stockArr);
        File sourceDir = new File("./" + RandomJavaGenerator.GENERATION_PATH + "/main/java");
        File[] compiled = sourceDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".class");
            }
        });
        for (File c : compiled) {
            c.renameTo(new File(outputDir + "/" + c.getName()));
        }
//        myFile.renameTo(new File("/the/new/place/newName.file"));

        return result;


    }

}
