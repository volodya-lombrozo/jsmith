package com.github.lombrozo.jsmith;

/**
 * This class allow the program to execute for a fixed time interval,
 * in the case the timeout is reached an InterruptedException is catch.
 */
public class Worker extends Thread {
    private final Process process;
    public Integer exit;

    public Worker(Process process) {
        this.process = process;
    }

    public void run() {
        try {
            exit = process.waitFor();
        } catch (InterruptedException ignore) {
            return;
        }
    }
}