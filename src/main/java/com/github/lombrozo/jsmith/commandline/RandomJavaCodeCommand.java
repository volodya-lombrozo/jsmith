package com.github.lombrozo.jsmith.commandline;

import com.github.lombrozo.jsmith.RandomJavaClass;
import picocli.CommandLine;

/**
 * @todo #18: Add more console commands and also write tests for them.
 */

@CommandLine.Command(name = "randomjavacode", description = "Generates a random Java code", mixinStandardHelpOptions = true)
public class RandomJavaCodeCommand implements Runnable{

    @CommandLine.Option(names = {"-s", "--seed"}, description = "seed for random generation", defaultValue = "-1")
    long seed;

    @Override
    public void run() {
        RandomJavaClass clazz;
        if (seed == -1) {
            clazz = new RandomJavaClass();
        } else {
            clazz = new RandomJavaClass(seed);
        }
        System.out.print(clazz.src());
    }
}