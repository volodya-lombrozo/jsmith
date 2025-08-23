package com.github.lombrozo.jsmith.it;

import com.github.lombrozo.jsmith.commandline.RandomJavaCodeCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.logging.Logger;

/**
 * This test checks how CLI works
 **/
public class ConsoleCommandIT {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private static final Logger logger = Logger.getLogger(ConsoleCommandIT.class.getName());

    @RepeatedTest(10)
    public void generatesJavaCode() {
        CommandLine cmd = new CommandLine(new RandomJavaCodeCommand());
        System.setOut(new PrintStream(outputStream));
        String randomExecutionArgs = getRandomExecuteArgs();
        int exitCode;
        if (randomExecutionArgs != null) {
            exitCode = cmd.execute(randomExecutionArgs);
        } else {
            exitCode = cmd.execute();
        }
        System.setOut(originalOut);
        String executionResult = outputStream.toString();

        Assertions.assertEquals(0, exitCode, "We expect the command to complete correctly.");
        Assertions.assertFalse(executionResult.isEmpty(), "We expect the output to be non-empty.");
        logger.info("Generated code: " + "\n-----\n" + executionResult + "\n-----\n");
    }

    private String getRandomExecuteArgs() {
        Random random = new Random();
        boolean needArgs = random.nextBoolean();
        long seed;
        if (needArgs) {
            seed = random.nextLong();
            logger.info("Random seed: " + seed);
            return "-s=" + seed;
        } else {
            return null;
        }
    }
}
