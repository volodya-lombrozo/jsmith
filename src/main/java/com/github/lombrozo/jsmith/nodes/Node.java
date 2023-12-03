package com.github.lombrozo.jsmith.nodes;

/**
 * This is the interface for all the possible nodes in the program.
 * Each production must be implemented following this structure.
 */
public interface Node {

    /**
     * This method allows the production of the source code.
     * Each node implementation must implement accordingly to the java grammar.
     * @return The production of this node
     */
    String produce();

}
